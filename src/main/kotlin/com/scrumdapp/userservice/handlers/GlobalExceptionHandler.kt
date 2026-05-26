package com.scrumdapp.userservice.handlers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler(
    private val exceptionHandler: ExceptionService
) {

//    @ExceptionHandler(AuthenticationServiceException::class)
//    fun handleAuthExceptions(ex: AuthenticationServiceException): ResponseEntity<ErrorResponse> {
//
//    }

    @ExceptionHandler(AppException::class)
    fun handleAppException(e: AppException): ResponseEntity<ErrorResponse> {
        val body = ErrorResponse(
            code = e.status.value(),
            message = e.message
        )
        return ResponseEntity<ErrorResponse>(body, e.status)
    }
}