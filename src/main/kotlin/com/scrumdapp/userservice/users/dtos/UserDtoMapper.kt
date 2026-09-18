package com.scrumdapp.userservice.users.dtos

import com.scrumdapp.userservice.users.Roles
import com.scrumdapp.userservice.users.User
import com.scrumdapp.userservice.users.PartialUser

fun User.toResponseDto(): UserResponseDto {
    val fullName = name.split(" ")
    return UserResponseDto(
        id = id,
        first_name = fullName.take(1).joinToString(),
        last_name = fullName.drop(1).joinToString(" "),
        roles = listOf(role.name),
        avatar = profilePicture,
    )
}

fun PartialUser.toResponseDto(): PartialUserResponseDto {
    val fullName = name.split(" ")
    return PartialUserResponseDto(
        id = id,
        first_name = fullName.take(1).joinToString(),
        last_name = fullName.drop(1).joinToString(" "),
    )
}

fun User.patchFromDto(dto: UserPatchDto): User = apply {
    dto.firstName?.let { name = it }
    dto.avatar?.let { profilePicture = it }
}

fun User.patchFromDto(dto: UserUpsertDto): User = apply {
    email = dto.email
    role = Roles.valueOf(dto.role.uppercase())
    dto.name?.let { name = it }
    dto.avatar?.let { profilePicture = it }
}

fun UserUpsertDto.toEntity(): User {
    return User().apply {

        email = this@toEntity.email
        this.name = this@toEntity.name.toString()
        profilePicture = this@toEntity.avatar
        role = Roles.valueOf(this@toEntity.role.uppercase())
    }
}