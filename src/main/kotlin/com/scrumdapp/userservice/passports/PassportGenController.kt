package com.scrumdapp.userservice.passports

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class PassportGenController(private val passportGenService: PassportGenService) {

    // This route is only approached from the gateway
    // Might be moved to a seperate service at a later point
    @GetMapping("/users/{userId}/passport")
    fun getUserPassport(
        @PathVariable userId: Long
    ): PassportDto {
        return passportGenService.generate(userId)
    }
}