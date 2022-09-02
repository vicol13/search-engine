package com.vvv.engine.advice

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.stereotype.Component


@Target(allowedTargets = [AnnotationTarget.FUNCTION])
@Retention(value = AnnotationRetention.RUNTIME)
annotation class TrackExecutionTime


/**
 * Tracks the execution time of a function and log it
 */
@Aspect
@Component
@ConditionalOnExpression("\${aspect.enabled:true}")
class ExecutionTimeAdvice {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Around("@annotation(TrackExecutionTime)")
    @Throws(Throwable::class)
    fun executionTime(point: ProceedingJoinPoint): Any {
        val startTime = System.currentTimeMillis()
        val obj: Any = point.proceed()
        val endTime = System.currentTimeMillis()
        logger.debug(
            "Class Name: " + point.signature.declaringTypeName + ". Method Name: " + point.signature
                .name + ". Time taken for Execution is : " + (endTime - startTime) + "ms"
        )
        return obj
    }
}