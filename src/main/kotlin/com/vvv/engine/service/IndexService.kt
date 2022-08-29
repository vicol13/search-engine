package com.vvv.engine.service

interface IndexService<T> {
    /**
     *  contract for index (inverted index) of corpus
     *  it can be from file or database
     */
    fun index(input: T)
}
