package com.vvv.engine.service

import com.vvv.engine.utils.Trie
import com.vvv.engine.utils.TrieException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class TrieTest {
    private val wordSet: Set<String> = setOf("man", "play", "point", "player")
    private val trie = Trie(wordSet)


    @Test
    fun `should return single result based on prefix`() {
        //when
        val result = trie.search("ma")
        //then
        Assertions.assertEquals(1, result.size)
        Assertions.assertEquals("man", result.first())
    }

    @Test
    fun `should ret single result based on full word`() {
        //when
        val result = trie.search("man")
        //then
        Assertions.assertEquals(1, result.size)
        Assertions.assertEquals("man", result.first())
    }

    @Test
    fun `should return 3 words with common prefix`() {
        //when
        val result = trie.search("p")
        //then
        Assertions.assertEquals(3, result.size)
        Assertions.assertTrue(result.contains("play"))
        Assertions.assertTrue(result.contains("point"))
        Assertions.assertTrue(result.contains("player"))
    }


    @Test
    fun `should return 2 word with common prefix`() {
        //when
        val result = trie.search("play")
        //then
        Assertions.assertEquals(2, result.size)
        Assertions.assertTrue(result.contains("play"))
        Assertions.assertTrue(result.contains("player"))
    }

    @Test
    fun `should return empty list when prefix is not trie`() {
        //when
        val result = trie.search("ago")
        //then
        Assertions.assertTrue(result.isEmpty())
    }

    @Test
    fun `should throw an error on blank search`() {
        Assertions.assertThrows(TrieException::class.java) {
            trie.search("")
        }
    }
}