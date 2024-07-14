package com.sixsense.fleetmanagement.advice

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException::class)
    fun handleGlobalRunTimeException(ex: Exception, request: WebRequest): ResponseEntity<Any> {
        val errorDetails = Result.failure<RuntimeException>(RuntimeException("Runtime Error Ocurred due to following reason $ex"))
        return ResponseEntity(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}