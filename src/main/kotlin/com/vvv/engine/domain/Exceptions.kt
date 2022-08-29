package com.vvv.engine.domain

import org.springframework.http.HttpStatus

class NoSuchWordException(message: String) : RuntimeException(message)

class EmptyCorpusException(message: String) : RuntimeException(message)

class NoSuchFileException(message: String) : RuntimeException(message)


data class ErrorMessage(
    val message: String,
    val timeStamp: String,
    val httpStatus: HttpStatus
)