package com.vvv.engine.service

interface Preprocessor<T> {
    fun toBaseForm(word: String): String
    fun clean(sentence: String): List<T>
}
