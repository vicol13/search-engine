package com.vvv.engine.service

import com.vvv.engine.domain.WordSearchResultDTO

interface SearchService {
    /**
     * search function which return result from inverted index
     *
     * @param word -> user inserted word
     */
    fun search(word: String): WordSearchResultDTO

}