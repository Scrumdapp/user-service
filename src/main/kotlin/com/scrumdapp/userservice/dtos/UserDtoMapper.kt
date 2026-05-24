package com.scrumdapp.userservice.dtos

import com.scrumdapp.userservice.entities.Roles
import com.scrumdapp.userservice.entities.User
import com.scrumdapp.userservice.repositories.PartialUser

fun User.toResponseDto(): UserResponseDto {
    val fullName = name.split(" ")
    return UserResponseDto(
        id = id,
        discordId = discordId,
        first_name = fullName.take(1).joinToString(),
        last_name = fullName.drop(1).joinToString(),
        avatar = profilePicture,
        role = role.name
    )
}

fun PartialUser.toResponseDto(): PartialUserResponseDto {
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
        this.name = this@toEntity.name.toString()
        profilePicture = this@toEntity.avatar
        role = Roles.valueOf(this@toEntity.role.uppercase())
    }
}