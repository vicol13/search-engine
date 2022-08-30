package com.vvv.engine.utils

import org.slf4j.LoggerFactory

class WeightedTrie(map: Map<String, Int>) : Trie(map.keys) {
    private val weights: Map<String, Float>

    init {
        val sum = map.map { it.value }.sum()
        weights = map.mapValues { it.value.toFloat() / sum }
    }

    override fun search(prefix: String): List<String> {
        val list = super.search(prefix)
        return list.sortedByDescending { this.weights[it] }
    }

}
