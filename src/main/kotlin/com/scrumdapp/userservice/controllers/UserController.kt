package com.scrumdapp.userservice.controllers

import com.scrumdapp.passportplugin.annotations.Passport
import com.scrumdapp.passportplugin.jwt.PassportContent
import com.scrumdapp.userservice.dtos.PartialUserResponseDto
import com.scrumdapp.userservice.dtos.PassportDto
import com.scrumdapp.userservice.dtos.UserUpsertDto
import com.scrumdapp.userservice.dtos.UserPatchDto
import com.scrumdapp.userservice.dtos.UserResponseDto
import com.scrumdapp.userservice.handlers.NoAccessException
import com.scrumdapp.userservice.services.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
) {
    @GetMapping()
    fun getUsers(
        @RequestParam(required = true) ids: List<Long>,
    ): List<PartialUserResponseDto> {
        val users = userService.getPartialByIds(ids)
        return users
    }

    @GetMapping("/@me")
    fun getSelf(
        @Passport passport: PassportContent
    ): UserResponseDto {
        return userService.getById(passport.userId.toLong())
    }

    @PatchMapping("/@me")
    fun patchSelf(
        @Passport passport: PassportContent,
        @Valid @RequestBody dto: UserPatchDto
    ): UserResponseDto {
        return userService.patchUser(dto, passport.userId.toLong())
    }

    @GetMapping("/{userId}")
    fun getUser(
        @PathVariable userId: Long,
    ): UserResponseDto {
        return userService.getById(userId)
    }

    // Only approached from the gateway
    @GetMapping("/{userId}/passport")
    fun getPassport(
        @PathVariable userId: Long,
    ): PassportDto {
        return userService.generatePassport(userId)
    }

    // Only approached from the gateway to register users
    @PatchMapping("/gateway")
    fun updateUser(
        @Passport passport: PassportContent,
        @RequestBody dto: UserUpsertDto
    ): UserResponseDto {
        val roles = passport.roles
        if (!roles.isNullOrEmpty() && !roles.contains("GATEWAY")) {
            throw Exception("No access")
        } else {
            return userService.upsertUser(dto)
        }
    }
}