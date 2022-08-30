package com.vvv.engine.service

import com.vvv.engine.utils.Trie
import com.vvv.engine.utils.WeightedTrie

class TriePrefixService(set: Set<String>) : PrefixService {
    private val trie: Trie

    init {
        this.trie = Trie(set)
    }

    override fun search(prefix: String): List<String> {
        return this.trie.search(prefix.lowercase())
    }

}


class WeightedTriePrefixService(map: Map<String, Int>) : PrefixService {
    private val trie: Trie

    init {
        this.trie = WeightedTrie(map)
    }

    override fun search(prefix: String): List<String> {
        return trie.search(prefix)
    }

}