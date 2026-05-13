package com.scrumdapp.userservice.dtos

import jakarta.validation.constraints.Size


data class UserResponseDto(
    val id: Long,
    val discordId: Long?,
    val firstName: String? = null,
    val lastName: String? = null,
    val avatar: String? = null,
    val role: String
)

data class PartialUserResponseDto(
    val id: Long,
    val firstName: String? = null,
    val lastName: String? = null,
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
    val firstName: String? = null,
    val lastName: String? = null,
    val avatar: String? = null,
    val role: String
)