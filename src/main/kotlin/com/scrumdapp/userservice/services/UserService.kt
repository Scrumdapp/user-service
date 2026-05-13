package com.scrumdapp.userservice.services

import com.scrumdapp.userservice.dtos.UserUpsertDto
import com.scrumdapp.userservice.dtos.PartialUserResponseDto
import com.scrumdapp.userservice.dtos.UserPatchDto
import com.scrumdapp.userservice.dtos.UserResponseDto
import com.scrumdapp.userservice.dtos.patchFromDto
import com.scrumdapp.userservice.dtos.toEntity
import com.scrumdapp.userservice.dtos.toPartialResponseDto
import com.scrumdapp.userservice.dtos.toResponseDto
import com.scrumdapp.userservice.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository){

    fun getById(id: Long): UserResponseDto {
        val user = userRepository.findUserById(id) ?: throw Exception()
        return user.toResponseDto()
    }

    fun getPartialById(id: Long): PartialUserResponseDto {
        val user = userRepository.findUserById(id) ?: throw Exception()
        return user.toPartialResponseDto()
    }

    fun getByDiscordId(id: Long): UserResponseDto {
        val user = userRepository.findDistinctByDiscordId(id) ?: throw Exception()
        return user.toResponseDto()
    }

    fun upsertUser(dto: UserUpsertDto): UserResponseDto {
        val user = userRepository.findDistinctByDiscordId(dto.discordId)
        if (user == null) {
            return userRepository.save(dto.toEntity()).toResponseDto()
        } else {
            user.patchFromDto(dto)
            return userRepository.save(user).toResponseDto()
        }
    }

    fun patchUser(dto: UserPatchDto, userId: Long): UserResponseDto {
        val user = userRepository.findUserById(userId) ?: throw Exception("User with id $userId not found")
        user.patchFromDto(dto)
        return userRepository.save(user).toResponseDto()
    }

}