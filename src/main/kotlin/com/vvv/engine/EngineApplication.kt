package com.vvv.engine

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.AnnotationConfigApplicationContext

@SpringBootApplication
class EngineApplication

fun main(args: Array<String>) {
	runApplication<EngineApplication>(*args)

}
