package com.scrumdapp.userservice.dtos

data class PassportDto(
    val userId: Long = 0,
    val userGroups: List<Long> = emptyList(),
    val roles: List<String> = emptyList()
)