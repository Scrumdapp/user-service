package com.scrumdapp.userservice.users

import com.scrumdapp.passportplugin.annotations.Passport
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
    @GetMapping("/@me")
    fun getCurrentUser(
        @Passport passport: PassportContent
    ): UserResponseDto {
        return userService.getSelf(passport.userId.toLong())
    }

    @PatchMapping("/@me")
    fun patchCurrentUser(
        @Passport passport: PassportContent,
        @Valid @RequestBody dto: UserPatchDto
    ): UserResponseDto {
        return userService.patch(dto, passport.userId.toLong())
    }

    @GetMapping("/{userId}")
    fun getUser(
        @PathVariable userId: Long,
        @Passport passport: PassportContent
    ): UserResponseDto {
        return userService.getById(userId, passport)
    }

    @GetMapping("/group")
    fun getGroup(
        @RequestParam(required = true) id: Long,
        @Passport passport: PassportContent
    ): List<PartialUserResponseDto> {
        if (passport.userGroups.isNullOrEmpty() || passport.userGroups?.contains(id.toInt()) == false) {
            throw NoAccessException(message = "You are not allowed to access this group")
        }

        return userService.getAllByGroup(id)
    }

    // Only approached from the gateway to register users
    @PatchMapping("/gateway")
    fun updateUser(
        @Passport passport: PassportContent,
        @RequestBody dto: UserUpsertDto
    ): UserResponseDto {
        val roles = passport.roles
        if (!roles.isNullOrEmpty() && !roles.contains("GATEWAY")) {
            throw NoAccessException(message = "Forbidden")
        } else {
            return userService.upsert(dto)
        }
    }
}