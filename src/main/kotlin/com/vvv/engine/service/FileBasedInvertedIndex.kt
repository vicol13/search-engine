package com.vvv.engine.service

import com.vvv.engine.domain.FileMap
import com.vvv.engine.domain.IndexedWord
import com.vvv.engine.domain.NoSuchWordException
import com.vvv.engine.domain.WordSearchResultDTO
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.File
import javax.annotation.PostConstruct


@Service
class FileBasedInvertedIndex(
    @Autowired
    private val preprocessor: Preprocessor<IndexedWord>,
    @Autowired
    private val fileService: FileService
) : SearchService, IndexService<File> {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val map = mutableMapOf<String, FileMap>()


    /**
     * Function which takes a file and parse it line by line
     * and then add each word to inverted index with corresponding
     */
    override fun index(input: File) {
        input.bufferedReader().use { buff ->
            buff.lineSequence()
                .withIndex()
                .map { IndexedValue(it.index, preprocessor.clean(it.value)) }
                .forEach { indexedSentence ->
                    //add to map at which line it is and in which file
                    indexedSentence.value.forEach { word -> this.put(word, input.name, indexedSentence.index) }
                }
        }
    }

    /**
     *  Defines how and what we store in inverted index
     *
     */
    private fun put(indexedWord: IndexedWord, fileName: String, line: Int) {
        this.map[indexedWord.word] = this.map.getOrDefault(indexedWord.word, FileMap())
            .also {
                it.update(fileName).update(line, indexedWord.indexes)
            }

    }


    /**
     * Search function it take a word turn it to base form
     * and collect the results from corpus
     *
     * todo (future improvement):
     *      1. in case of adding pagination it should be added here
     *      before data is loaded from file
     */
    override fun search(word: String): WordSearchResultDTO {
        val baseForm = this.preprocessor.toBaseForm(word)
        val occurrences = this.map[baseForm]
            ?: throw NoSuchWordException("Word [$word] does not exist in index")

        return WordSearchResultDTO(
            word = word,
            files = occurrences.entries().map { this.fileService.get(it.key, it.value) }
        )

    }


    /**
     *  it triggers the index when spring boot build context
     *
     * todo:
     *  1. save map into dict and then do checksum if it is okay
     *      then unmarshall it otherwise re-index all corpus
     */
    @PostConstruct
    fun index() {
        logger.info("Re-Indexing corpus")
        fileService.loadCorpus()
            .forEach { index(it) }
        logger.info("Re-Indexing corpus is done")
        logger.info("Indexed [${this.map.keys.size}] words")
    }

    /**
     * return the word and number of occurrences used for building the trie
     */
    fun getStats() = this.map.mapValues { it.value.getOccurences() }
}
