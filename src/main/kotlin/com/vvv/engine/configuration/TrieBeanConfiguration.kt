package com.vvv.engine.configuration

import com.vvv.engine.service.FileBasedInvertedIndex
import com.vvv.engine.service.PrefixService
import com.vvv.engine.service.TriePrefixService
import com.vvv.engine.service.WeightedTriePrefixService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class TrieBeanConfiguration {


    @Bean
    fun trieService(
        @Autowired indexService: FileBasedInvertedIndex
    ): PrefixService {
        return TriePrefixService(indexService.getStats().keys)
    }


    @Bean
    @Primary
    fun weightedTrieService(
        @Autowired indexService: FileBasedInvertedIndex
    ): PrefixService {
        return WeightedTriePrefixService(indexService.getStats())
    }

}