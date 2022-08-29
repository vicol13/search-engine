package com.vvv.engine.service

import com.vvv.engine.domain.EmptyCorpusException
import com.vvv.engine.domain.FileSearchDTO
import com.vvv.engine.domain.LineMap
import com.vvv.engine.domain.NoSuchFileException
import com.vvv.engine.domain.SentenceSearchDTO
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File

@Service
class FileService(
    @Value("\${nlp.paths.corpus}")
    private val corpusPath: String
) {

    /**
     * returns all files from corpus
     */
    fun loadCorpus(): List<File> {
        return File(this.corpusPath)
            .walkTopDown()
            .filter { it.isFile }
            .filter { it.extension == "txt" }
            .toList()
            .takeIf { it.isNotEmpty() }
            ?: throw EmptyCorpusException("Corpus at [${corpusPath}] is empty")
    }

    /**
     * Load content from file based on a line map
     * @param fileName from which data should be extracted
     * @param map contains line which have to be extracted
     * and indexes of the words which will be also added in response
     */
    fun get(fileName: String, map: LineMap): FileSearchDTO {
        return File("$corpusPath/$fileName")
            .takeIf { it.exists() }
            ?.let { collectLines(it, map.keys()) }
            ?.let { toFileSearchDto(fileName, it, map) }
            ?: throw NoSuchFileException("File [$fileName] does not exist")
    }

    /**
     * Build a FileSearchDTO from collected data from file
     *
     * @param fileName is representing file name from which data was extracted
     * @param sentenceMap mach which contains line number and sentence at this line
     * @param lineMap contains line on which we have the word and at which indexes
     *
     * @return sentences aggregate with indexes where the words are by file name
     */
    private fun toFileSearchDto(
        fileName: String, sentenceMap: Map<Int, String>, lineMap: LineMap
    ): FileSearchDTO {
        return FileSearchDTO(
            fileName = fileName,
            sentences = sentenceMap.map {
                SentenceSearchDTO(
                    line = it.key,
                    sentence = it.value,
                    indexes = lineMap.get(it.key)!!
                )
            }
        )

    }

    /**
     *
     *  Function which extracts read file and return line number as key
     *  and line content as value
     *
     *  @param file from which data will be extracted
     *  @param lines set of line which shall be extracted
     *
     * todo (possible performance optimizations):
     *  1. add validation
     *      a. if counter is higher than max element in set stop
     *      b. max element of set might be higher than number of lines
     *
     */
    private fun collectLines(file: File, lines: Set<Int>): MutableMap<Int, String> {
        val collectingMap = mutableMapOf<Int, String>()
        var counter = 0
        file.bufferedReader()
            .use {
                it.forEachLine { line ->
                    if (lines.contains(counter)) collectingMap[counter] = line
                    counter += 1
                }
            }
        return collectingMap
    }

}

