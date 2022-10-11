package com.vvv.engine.service

import com.vvv.engine.domain.FileSearchDTO
import com.vvv.engine.domain.WordSearchResultDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface SearchService {
    /**
     * search function which return result from inverted index
     *
     * @param word -> user inserted word
     */
    fun search(word: String): WordSearchResultDTO

    fun search(word: String, pageable: Pageable): Page<FileSearchDTO>

}