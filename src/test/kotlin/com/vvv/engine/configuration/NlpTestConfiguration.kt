package com.vvv.engine.configuration

import com.vvv.engine.domain.IndexedWord
import com.vvv.engine.service.IndexedLemmaPreprocessor
import com.vvv.engine.service.Preprocessor
import opennlp.tools.lemmatizer.DictionaryLemmatizer
import opennlp.tools.postag.POSModel
import opennlp.tools.postag.POSTaggerME
import opennlp.tools.tokenize.SimpleTokenizer
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.PropertySource
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.io.File
import java.io.FileInputStream


@Configuration
class NlpTestConfiguration{
    @Bean
    fun stopWords(): Set<String> {
        return File("src/test/resources/nlp/stop-words.txt")
            .bufferedReader()
            .readLines()
            .toSet()
    }


    @Bean
    fun posTagger(): POSTaggerME {
        return FileInputStream("src/test/resources/nlp/en-pos-maxent.bin")
            .let { POSModel(it) }
            .let { POSTaggerME(it) }
    }


    @Bean
    fun lemmatizer(): DictionaryLemmatizer {
        return FileInputStream("src/test/resources/nlp/lemma_dict")
            .let { DictionaryLemmatizer(it) }
    }


    @Bean
    fun tokenizer(): SimpleTokenizer {
        return SimpleTokenizer.INSTANCE
    }

    @Bean
    fun preprocessor(
        tokenizer: SimpleTokenizer,
        lemmatizer: DictionaryLemmatizer,
        posTaggerME: POSTaggerME,
        stopWords: Set<String>
    ): Preprocessor<IndexedWord> {
        return IndexedLemmaPreprocessor(
            posTagger = posTaggerME,
            lemmatizer = lemmatizer,
            stopWords = stopWords,
            tokenizer = tokenizer
        )
    }
}