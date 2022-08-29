package com.vvv.engine.configuration

import opennlp.tools.lemmatizer.DictionaryLemmatizer
import opennlp.tools.postag.POSModel
import opennlp.tools.postag.POSTaggerME
import opennlp.tools.tokenize.SimpleTokenizer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import java.io.File
import java.io.FileInputStream


@Configuration
class NlpConfiguration(
    @Value("\${nlp.paths.lemma_dict}")
    private val lemmatizerPath: String,
    @Value("\${nlp.paths.pos_tags}")
    private val posTagsPath: String,
    @Value("\${nlp.paths.stop_words}")
    private val stopWordsPath: String
) {
    @Bean
    @Lazy(true)
    fun stopWords(): Set<String> {
        return File(this.stopWordsPath)
            .bufferedReader()
            .readLines()
            .toSet()
    }


    @Bean
    @Lazy(true)
    fun posTagger(): POSTaggerME {
        return FileInputStream(this.posTagsPath)
            .let { POSModel(it) }
            .let { POSTaggerME(it) }
    }


    @Bean
    @Lazy(true)
    fun lemmatizer(): DictionaryLemmatizer {
        return FileInputStream(this.lemmatizerPath)
            .let { DictionaryLemmatizer(it) }
    }


    @Bean
    @Lazy(true)
    fun tokenizer(): SimpleTokenizer {
        return SimpleTokenizer.INSTANCE
    }
}
