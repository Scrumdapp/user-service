package com.scrumdapp.userservice.handlers

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

class NotAuthorizedException(
    override val status: HttpStatus = HttpStatus.UNAUTHORIZED,
    override val message: String
): AppException(status, message)

class NoAccessException(
    override val status: HttpStatus = HttpStatus.FORBIDDEN,
    override val message: String
): AppException(status, message)

class NotFoundException(
    override val status: HttpStatus = HttpStatus.NOT_FOUND,
    override val message: String
): AppException(status, message)

class ServerFaultException(
    override val status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
    override val message: String
): AppException(status, message)

class ServiceUnavailableException(
    override val status: HttpStatus = HttpStatus.SERVICE_UNAVAILABLE,
    override val message: String
): AppException(status, message)

open class AppException(
    open val status: HttpStatus,
    override val message: String = status.name
): RuntimeException()
