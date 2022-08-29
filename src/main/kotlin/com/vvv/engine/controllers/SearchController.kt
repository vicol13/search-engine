package com.vvv.engine.controllers

import com.vvv.engine.domain.WordSearchResultDTO
import com.vvv.engine.service.SearchService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController


@RestController
class SearchController(
    @Autowired private val searchService: SearchService
) {

    @GetMapping("/search/{word}")
    @ResponseStatus(HttpStatus.OK)
    fun search(@PathVariable("word") word: String): WordSearchResultDTO {
        return this.searchService.search(word)
    }
}