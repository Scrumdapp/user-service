package com.scrumdapp.userservice.dtos

import com.scrumdapp.userservice.entities.Roles
import com.scrumdapp.userservice.entities.User

fun User.toResponseDto(): UserResponseDto {
    return UserResponseDto(
        id = id,
        discordId = discordId,
        name = name,
        avatar = profilePicture,
        role = role.name
    )
}

fun User.toPartialResponseDto(): PartialUserResponseDto {
    return PartialUserResponseDto(
        id = id,
        name = name,
    )
}

fun User.patchFromDto(dto: UserPatchDto): User = apply {
    dto.firstName?.let { name = it }
    dto.avatar?.let { profilePicture = it }
}

fun User.patchFromDto(dto: UserUpsertDto): User = apply {
    discordId = dto.discordId
    role = Roles.valueOf(dto.role.uppercase())
    dto.name?.let { name = it }
    dto.avatar?.let { profilePicture = it }
}

fun UserUpsertDto.toEntity(): User {
    return User().apply {

        discordId = this@toEntity.discordId
        this.name = this@toEntity.name
        profilePicture = this@toEntity.avatar
        role = Roles.valueOf(this@toEntity.role.uppercase())
    }
}