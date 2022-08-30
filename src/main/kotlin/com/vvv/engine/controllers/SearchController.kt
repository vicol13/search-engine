package com.vvv.engine.controllers

import com.vvv.engine.domain.WordSearchResultDTO
import com.vvv.engine.service.PrefixService
import com.vvv.engine.service.SearchService
import com.vvv.engine.service.TriePrefixService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

import java.lang.RuntimeException
import javax.websocket.server.PathParam


@RestController
class SearchController(
    @Autowired private val searchService: SearchService,
    @Autowired private val prefixService: PrefixService
) {

    @GetMapping("/search/{word}")
    @ResponseStatus(HttpStatus.OK)
    fun search(@PathVariable("word") word: String): WordSearchResultDTO {
        return this.searchService.search(word)
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    fun getWords(
        @PathParam("prefix") prefix: String?
    ): List<String> {
        if (prefix.isNullOrBlank() || prefix.isEmpty()) {
            throw RuntimeException("specify prefix for")
        }
        return this.prefixService.search(prefix)
    }
}