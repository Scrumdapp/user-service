package com.scrumdapp.userservice.dtos

import com.scrumdapp.userservice.entities.Roles
import com.scrumdapp.userservice.entities.User

fun User.toResponseDto(): UserResponseDto {
    return UserResponseDto(
        id = id,
        discordId = discordId,
        firstName = firstName,
        lastName = lastName,
        avatar = profilePicture,
        role = role.name
    )
}

fun User.patchFromDto(dto: UserPatchDto): User = apply {
    dto.firstName?.let { firstName = it }
    dto.lastName?.let { lastName = it }
    dto.avatar?.let { profilePicture = it }
}

fun UserCreateDto.toEntity(): User {
    return User().apply {

        discordId = this@toEntity.discordId
        firstName = this@toEntity.firstName
        lastName = this@toEntity.lastName
        profilePicture = this@toEntity.avatar
        role = Roles.valueOf(this@toEntity.role.uppercase())
    }
}