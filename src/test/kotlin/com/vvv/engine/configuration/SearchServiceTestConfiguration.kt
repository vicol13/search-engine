package com.vvv.engine.configuration

import com.vvv.engine.domain.IndexedWord
import com.vvv.engine.service.FileBasedInvertedIndex
import com.vvv.engine.service.FileService
import com.vvv.engine.service.Preprocessor
import com.vvv.engine.service.SearchService
import org.mockito.Mockito
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class SearchServiceTestConfiguration {

    @Bean
    fun preprocessor(): Preprocessor<IndexedWord> {
        return Mockito.mock(Preprocessor::class.java) as Preprocessor<IndexedWord>
    }

    @Bean
    fun fileService(): FileService {
        return Mockito.mock(FileService::class.java)
    }

    @Bean
    fun searchService(
        preprocessor: Preprocessor<IndexedWord>,
        fileService: FileService
    ): SearchService {
        return FileBasedInvertedIndex(preprocessor, fileService)
    }
}