package com.vvv.engine.service

import com.vvv.engine.configuration.NlpTestConfiguration
import com.vvv.engine.domain.Index
import com.vvv.engine.domain.IndexedWord
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension


@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [NlpTestConfiguration::class])
class PreprocessorTest {

    @Autowired
    private lateinit var preprocessor: Preprocessor<IndexedWord>


    @Test
    fun `should return base form of an word`() {
        Assertions.assertEquals("person", preprocessor.toBaseForm("people"))
    }

    @Test
    fun `should remove stop words from sentence and return others in base form`() {
        //given (added some mistakes to validate the preprocessor)
        val phrase = "Currently, I have to use an feed each day my cats as they are eating a lots of wiskas"
        //when
        val result = preprocessor.clean(phrase)
        //then
        Assertions.assertArrayEquals(arrayOf("feed", "day", "cat", "eat", "lot", "wiskas"), result.map { it.word }.toTypedArray())
    }

    @Test
    fun `should return word once which repeated more than one time within phrase`() {
        //given (play is repeated twice in 2 different forms)
        val phrase = "he plays with his wife while his kids are playing outside"
        //when
        val result = preprocessor.clean(phrase)
        //then
        Assertions.assertArrayEquals(arrayOf("play", "wife", "kid"), result.map { it.word }.toTypedArray())
    }


    @Test
    fun `should remove stop words from sentence and return others in base form with indexes in the phrase`() {
        //given
        val phrase = "My cats, dogs are playing like kids"
        //when
        val result = preprocessor.clean(phrase)
        //then
        Assertions.assertEquals(4, result.size)
        Assertions.assertEquals(IndexedWord("cat", mutableListOf(Index(2, 6))), result[0])
        Assertions.assertEquals(IndexedWord("dog", mutableListOf(Index(7, 11))), result[1])
        Assertions.assertEquals(IndexedWord("play", mutableListOf(Index(14, 21))), result[2])
        Assertions.assertEquals(IndexedWord("kid", mutableListOf(Index(25, 29))), result[3])

    }


    @Test
    fun `should return word once which repeated more than one time within phrase with index`() {
        //given (play is repeated twice in 2 different forms)
        val phrase = "he plays with his wife while his kids are playing outside"
        //when
        val result = preprocessor.clean(phrase)
        //then
        print(result)
        Assertions.assertEquals(3, result.size)
        Assertions.assertEquals(2, result[0].indexes.size)
        Assertions.assertEquals(IndexedWord("play", mutableListOf(Index(2, 7), Index(33, 40))), result[0])
        Assertions.assertEquals(IndexedWord("wife", mutableListOf(Index(14, 18))), result[1])
        Assertions.assertEquals(IndexedWord("kid", mutableListOf(Index(26, 30))), result[2])
//        Assertions.assertArrayEquals(arrayOf("play", "wife", "kid"), result.toTypedArray())
    }
}