package com.vvv.engine.domain

data class WordSearchResultDTO(
    val word: String,
    val files: List<FileSearchDTO>,
    val occurrences: Int = files.map { it.sentences }
        .flatten().sumOf { it.indexes.size }
)

data class FileSearchDTO(
    val fileName: String,
    val sentences: List<SentenceSearchDTO>
)

data class SentenceSearchDTO(
    val sentence: String,
    val line: Int,
    val indexes: List<Index>
)

