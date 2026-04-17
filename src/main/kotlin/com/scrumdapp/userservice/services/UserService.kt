package com.scrumdapp.userservice.services

import com.scrumdapp.userservice.dtos.UserCreateDto
import com.scrumdapp.userservice.dtos.UserPatchDto
import com.scrumdapp.userservice.dtos.UserResponseDto
import com.scrumdapp.userservice.dtos.patchFromDto
import com.scrumdapp.userservice.dtos.toResponseDto
import com.scrumdapp.userservice.entities.Roles
import com.scrumdapp.userservice.repositories.UserRepository
import org.springframework.stereotype.Service

data class PartialUser(
    val id: Long,
    val role: Roles
)

interface UserService {
    fun getById(id: Long): UserResponseDto?
    fun getByDiscordId(id: Long): UserResponseDto?

    fun createUser(dto: UserCreateDto): UserResponseDto
    fun patchUser(dto: UserPatchDto, id: Long, ownId: Long): UserResponseDto?
}

@Service
class UserServiceImpl(private val userRepository: UserRepository) : UserService {

    override fun getById(id: Long): UserResponseDto? {
        return userRepository.findUserById(id)?.toResponseDto()

    }

    override fun getByDiscordId(id: Long): UserResponseDto? {
        return userRepository.findByDiscordId(id).firstOrNull()?.toResponseDto()
    }

    override fun createUser(dto: UserCreateDto): UserResponseDto {
        TODO("Not yet implemented")
    }

    override fun patchUser(
        dto: UserPatchDto,
        id: Long,
        ownId: Long,
    ): UserResponseDto? {

        if (ownId != id) throw Exception()

        val user = userRepository.findUserById(id) ?: throw Exception()
        return userRepository.save(user.patchFromDto(dto)).toResponseDto()
    }

}