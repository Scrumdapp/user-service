package com.scrumdapp.userservice.dtos

import com.scrumdapp.userservice.entities.Roles

data class UserResponseDto(
    val id: Long,
    val discordId: Long?,

    val firstName: String? = null,
    val lastName: String? = null,

    val avatar: String? = null,

    val role: String
)

data class UserPatchDto(

    val discordId: Long? = null,
    val firstName: String? = null,
    val lastName: String? = null,

    val avatar: String? = null,
)

data class UserCreateDto(

    val discordId: Long,
    val firstName: String? = null,
    val lastName: String? = null,
    val avatar: String? = null,
    val role: String
)