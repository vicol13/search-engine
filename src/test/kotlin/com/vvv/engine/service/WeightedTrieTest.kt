package com.vvv.engine.service

import com.vvv.engine.utils.WeightedTrie
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class WeightedTrieTest {
    private val map: Map<String, Int> =
        mapOf<String, Int>("man" to 10, "play" to 200, "point" to 100, "player" to 50)
    private val trie = WeightedTrie(map)


    @Test
    fun `should return 3 words sorted based on weight`() {
        //when
        val result = trie.search("p")
        //then
        Assertions.assertEquals(3, result.size)
        Assertions.assertEquals("play",result[0])
        Assertions.assertEquals("point",result[1])
        Assertions.assertEquals("player",result[2])
    }


    @Test
    fun `should return 2 words sorted based on weight`() {
        //when
        val result = trie.search("pl")
        //then
        Assertions.assertEquals(2, result.size)
        Assertions.assertEquals("play",result[0])
        Assertions.assertEquals("player",result[1])
    }
}