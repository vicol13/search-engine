package com.vvv.engine.service

import com.vvv.engine.advice.TrackExecutionTime
import com.vvv.engine.domain.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
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
     *  for now we just keep at which line and at which indexes
     *  are word
     */
    private fun put(indexedWord: IndexedWord, fileName: String, line: Int) {
        this.map[indexedWord.word] = this.map.getOrDefault(indexedWord.word, FileMap())
            .also {
                it.update(fileName).update(line, indexedWord.indexes)
            }

    }


    /**
     * get base form of a word and then return FileMap
     * which contains all occurrences of base form
     * of the word
     */
    private fun getOccurrences(word: String): FileMap {
        val baseForm = this.preprocessor.toBaseForm(word)
        return this.map[baseForm]
            ?: throw NoSuchWordException("Word [$word] does not exist in index")

    }

    /**
     * Return full search
     */
    @TrackExecutionTime
    @Deprecated("Use paginated instead")
    override fun search(word: String): WordSearchResultDTO {
        val fileSearchResult = getOccurrences(word)
            .entries()
            .map { this.fileService.get(it.key, it.value) }

        return WordSearchResultDTO(
            word = word,
            files = fileSearchResult
        )
    }


    @TrackExecutionTime
    override fun search(word: String, pageable: Pageable): Page<FileSearchDTO> {
        val content = getOccurrences(word).entries().toList()
        val sublist = content
            .let { it.subList(pageable.startIndex(), pageable.endIndex(it.size)) }
            .map { this.fileService.get(it.key, it.value) }

        return PageImpl(sublist, pageable, content.size.toLong())
    }

    private fun Pageable.startIndex(): Int {
        return if (pageNumber == 0) 0 
            else ((pageNumber - 1) * pageSize)
    }

    private fun Pageable.endIndex(maxSize: Int): Int {
        val endIndex = if (pageNumber == 0) pageSize else pageNumber * pageSize
        return if (endIndex > maxSize) maxSize else endIndex
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
