package com.scrumdapp.userservice.dtos

import jakarta.validation.constraints.Size

data class UserResponseDto(
    val id: Long,
    val first_name: String,
    val last_name: String,
    val roles: List<String>,
    val avatar: String? = null,
)

data class PartialUserResponseDto(
    val id: Long,
    val name: String? = null,
)

data class UserPatchDto(

    @Size(max = 50)
    val firstName: String? = null,
    @Size(max = 100)
    val lastName: String? = null,

    val avatar: String? = null,
)

data class UserUpsertDto(

    val discordId: Long,
    val name: String? = null,
    val avatar: String? = null,
    val role: String
)