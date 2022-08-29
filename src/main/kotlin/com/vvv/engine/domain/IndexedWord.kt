package com.vvv.engine.domain

/**
 * Data classes used to mark position of a word within sentence
 */
data class IndexedWord(val word: String, val indexes: MutableList<Index> = mutableListOf()) {

    constructor(word: String, start: Int, end: Int) : this(word) {
        this.indexes.add(Index(start, end))
    }
}

data class Index(val begin: Int, val end: Int) {
}