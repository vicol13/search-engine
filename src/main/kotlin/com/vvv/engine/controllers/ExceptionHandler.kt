package com.vvv.engine.controllers

import com.vvv.engine.domain.EmptyCorpusException
import com.vvv.engine.domain.ErrorMessage
import com.vvv.engine.domain.NoSuchWordException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@ControllerAdvice
class Advice(
    @Value("\${time_format}")
    private val timeFormat: String,

    @Autowired
    private val contextApplicationContext: ApplicationContext

) {

    private val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(timeFormat)

    @ExceptionHandler
    fun handleNoSuchWordException(ex: NoSuchWordException): ResponseEntity<ErrorMessage> {
        val message = ErrorMessage(
            message = ex.message!!,
            timeStamp = LocalDateTime.now().format(timeFormatter).toString(),
            httpStatus = HttpStatus.NOT_FOUND
        )
        return ResponseEntity(message, HttpStatus.NOT_FOUND)
    }


    @ExceptionHandler
    fun handleEmptyCorpus(ex: EmptyCorpusException): ResponseEntity<ErrorMessage> {
        val message = ErrorMessage(
            message = ex.message!!,
            timeStamp = LocalDateTime.now().format(timeFormatter).toString(),
            httpStatus = HttpStatus.SERVICE_UNAVAILABLE
        )

        return ResponseEntity(message, HttpStatus.SERVICE_UNAVAILABLE)
    }


}