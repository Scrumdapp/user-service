package com.scrumdapp.userservice.handlers

import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Component
import tools.jackson.databind.ObjectMapper

data class ErrorResponse(
    val code: Int,
    val message: String
)

@Component
class ExceptionService() {

    private val objectMapper: ObjectMapper = ObjectMapper()

    fun handleException(res: HttpServletResponse, ex: Throwable?) {
        val body = mapException(ex)

        res.status = body.code
        res.contentType = MediaType.APPLICATION_JSON_VALUE

        objectMapper.writeValue(res.outputStream, body)
    }

    private fun mapException(ex: Throwable?): ErrorResponse {

        if (ex == null) {
            return bodyFromHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        }

        return when (ex) {
            is AppException -> {
                println(ex.status)
                ErrorResponse(
                    code = ex.status.value(),
                    message = ex.message
                )
            }
            is AuthenticationException -> {
                bodyFromHttpStatus(HttpStatus.UNAUTHORIZED)
            }
            is AccessDeniedException -> {
                bodyFromHttpStatus(HttpStatus.FORBIDDEN, ex.message)
            }
            else -> {
                bodyFromHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
            }
        }
    }

    private fun bodyFromHttpStatus(
        httpStatusCode: HttpStatus,
        message: String? = httpStatusCode.name.lowercase()): ErrorResponse {
        return ErrorResponse(
            httpStatusCode.value(),
            message ?: httpStatusCode.name.lowercase()
        )
    }
}