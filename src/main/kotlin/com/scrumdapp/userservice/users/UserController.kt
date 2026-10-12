package com.scrumdapp.userservice.users

import com.scrumdapp.passportplugin.annotations.passportController.Passport
import com.scrumdapp.passportplugin.jwt.PassportContent
import com.scrumdapp.userservice.exceptions.NoAccessException
import com.scrumdapp.userservice.users.dtos.PartialUserResponseDto
import com.scrumdapp.userservice.users.dtos.UserPatchDto
import com.scrumdapp.userservice.users.dtos.UserResponseDto
import com.scrumdapp.userservice.users.dtos.UserUpsertDto
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
) {

    @GetMapping
    fun getUsers(
        @RequestParam(required = true) ids: List<Long>,
    ): List<PartialUserResponseDto> {
        val users = userService.getUsersByIds(ids)
        return users
    }

    @GetMapping("/@me")
    fun getCurrentUser(
        @Passport passport: PassportContent
    ): UserResponseDto {
        return userService.getUserById(passport.userId, passport)
    }

    @PatchMapping("/@me")
    fun patchCurrentUser(
        @Passport passport: PassportContent,
        @Valid @RequestBody dto: UserPatchDto
    ): UserResponseDto {
        return userService.patchUser(dto, passport.userId)
    }

    @GetMapping("/{id}")
    fun getUserById(
        @Passport passport: PassportContent,
        @PathVariable id: Long
    ): UserResponseDto {
        return userService.getUserById(id, passport)
    }

    @GetMapping("/users")
    fun getGroup(
        @RequestParam(required = true) groupId: Long,
        @Passport passport: PassportContent
    ): List<PartialUserResponseDto> {
        if (passport.userGroups.isEmpty() || passport.userGroups.contains(groupId)) {
            throw NoAccessException(message = "You are not allowed to access this group")
        }

        return userService.getUsersByGroup(groupId)
    }

    // Only approached from the gateway to register users
    @PatchMapping("/gateway")
    fun updateUser(
        @RequestBody dto: UserUpsertDto
    ): UserResponseDto {
        return userService.upsertUser(dto)
    }
}