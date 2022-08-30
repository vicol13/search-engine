package com.vvv.engine.utils

/**
 * This implementation of weight trie is not the best one
 * as we have to sort each results
 * todo :
 *  1. find better implementation (each node keep a list or a matrix with kids and their weight)
 */
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
