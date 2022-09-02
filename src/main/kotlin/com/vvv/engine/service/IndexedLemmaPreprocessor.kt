package com.vvv.engine.service

import com.vvv.engine.domain.IndexedWord
import opennlp.tools.lemmatizer.DictionaryLemmatizer
import opennlp.tools.postag.POSTaggerME
import opennlp.tools.tokenize.SimpleTokenizer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.Exception
import java.lang.RuntimeException

/**
 * todo:
 *  1. add is ascii validation
 */
@Service
class IndexedLemmaPreprocessor(
    @Autowired private val posTagger: POSTaggerME,
    @Autowired private val stopWords: Set<String>,
    @Autowired private val lemmatizer: DictionaryLemmatizer,
    @Autowired private val tokenizer: SimpleTokenizer
) : Preprocessor<IndexedWord> {
    private val PUNCTUATION = setOf(",", "!", "?", ".", "'", "\"", "’", "—", "", ":", ";")



    private fun isNotStopWord(word: String): Boolean {
        return !this.stopWords.contains(word)
    }


    /**
     * Function which return base form (lemma) of a single word
     */
    override fun toBaseForm(word: String): String {
        return this.lemmatize(arrayOf(word)).first()
    }


    /**
     *  function which clean sentence, like remove stop word
     *  punctuation and return words in base form with their indexes
     *  within sentence
     *
     *  @param sentence input to be clean
     *  @return list of IndexedWords
     */
    override fun clean(sentence: String): List<IndexedWord> {
        return sentence
            .let { tokenizer.tokenizePos(it) }
            .map { IndexedWord(sentence.byIndex(it.start, it.end), it.start, it.end) }
            .let { lemmatizeIndexed(it.toTypedArray()) }
            .filter { !PUNCTUATION.contains(it.word) && isNotStopWord(it.word) }
            .groupingBy { it.word }
            .aggregate { _, accumulator: IndexedWord?, element: IndexedWord, first: Boolean ->
                val result = if (first) element else accumulator.also { it!!.indexes.addAll(element.indexes) }
                return@aggregate result as IndexedWord
            }
            //as we have Map<String,IndexedWords> we return
            //only the values
            .map { it.value }

    }

    private fun String.byIndex(start: Int, end: Int): String {
        return this.subSequence(start, end).toString()
    }

    /**
     *  Apply lemmatization and POS tagging to a sentence
     *  if a word is already at base form then opennlp DictionaryLemmatizer
     *  will return O (not zero) that why we have validation in mapping
     *
     * @param sentence tokenized sentence
     */
    private fun lemmatize(sentence: Array<String>): List<String> {
        val posTags = this.posTagger.tag(sentence)
        val lemmas = this.lemmatizer.lemmatize(sentence, posTags)

        return lemmas.mapIndexed { index, it ->
            return@mapIndexed if (it == "O") sentence[index] else it
        }
    }

    /**
     * similar to lemmatize but it returns and indexes
     */
    private fun lemmatizeIndexed(sentence: Array<IndexedWord>): List<IndexedWord> {
        val words = sentence.map { it.word }.toTypedArray()
        val posTags = this.posTagger.tag(words)
        val lemmas = this.lemmatizer.lemmatize(words, posTags)

        return lemmas.mapIndexed { index, lemma ->
            return@mapIndexed if (lemma == "O") sentence[index]
            else sentence[index].copy(word = lemma)
        }
    }
}


