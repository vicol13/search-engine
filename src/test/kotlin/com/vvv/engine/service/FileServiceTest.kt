package com.vvv.engine.service

import com.vvv.engine.domain.EmptyCorpusException
import com.vvv.engine.domain.Index
import com.vvv.engine.domain.LineMap
import com.vvv.engine.domain.NoSuchFileException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class FileServiceTest {
    private val root = "src/test/resources/file-service"

    @Test
    fun `should throw and error when corpus is empty`() {
        Assertions.assertThrows(EmptyCorpusException::class.java) {
            FileService("$root/empty-corpus").loadCorpus()
        }
    }

    @Test
    fun `should return list of files`() {
        FileService("$root/big-corpus")
            .loadCorpus()
            .run { Assertions.assertEquals(6, this.size) }
    }


    @Test
    fun `should return 2 files`() {
        //given & when
        val corpus = FileService("$root/2-file-corpus")
            .loadCorpus()

        //then
        Assertions.assertEquals("blue-dog.txt", corpus[1].name)
        Assertions.assertEquals("yellow-cat.txt", corpus[0].name)

    }

    @Test
    fun `should throw error if file does not exist in corpus`() {
        //given
        val service = FileService("$root/2-file-corpus")

        //when
        Assertions.assertThrows(NoSuchFileException::class.java) {
            service.get("non-existing-name.txt", LineMap())
        }

    }

    @Test
    fun `should return indexed `() {
        //given
        val lineMap = LineMap()
            .apply { this.update(0, listOf(Index(86, 97), Index(62, 73))) }
        val service = FileService("$root/2-file-corpus")
        val phrase =
            "We need to see what is there, not what people are telling the development team or how developers assume."
        //when
        val result = service.get("blue-dog.txt", lineMap)

        //then
        Assertions.assertEquals(1, result.sentences.size)
        Assertions.assertEquals(phrase, result.sentences[0].sentence)
        Assertions.assertEquals(Index(86, 97), result.sentences[0].indexes[0])
        Assertions.assertEquals(Index(62, 73), result.sentences[0].indexes[1])
    }


}