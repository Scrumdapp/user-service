package com.scrumdapp.userservice.controllers

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController {

    @GetMapping("/@me")
    fun getOwnUser() {

    }

    @GetMapping("/{userId}")
    fun getUser(
        @PathVariable userId: Long,
        @AuthenticationPrincipal jwt: Jwt
    ) {
        val role = SecurityContextHolder.getContext().authentication?.authorities
        if (role != null && role.first().toString() == "ROLE_STUDENT") {

        }
    }

    @PatchMapping("/{userId}")
    fun updateUser(@PathVariable userId: Long) {

    }
}