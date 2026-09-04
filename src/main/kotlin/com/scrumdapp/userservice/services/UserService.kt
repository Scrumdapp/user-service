package com.scrumdapp.userservice.services

import com.scrumdapp.userservice.dtos.UserUpsertDto
import com.scrumdapp.userservice.dtos.PartialUserResponseDto
import com.scrumdapp.userservice.dtos.PassportDto
import com.scrumdapp.userservice.dtos.UserPatchDto
import com.scrumdapp.userservice.dtos.UserResponseDto
import com.scrumdapp.userservice.dtos.patchFromDto
import com.scrumdapp.userservice.dtos.toEntity
import com.scrumdapp.userservice.dtos.toResponseDto
import com.scrumdapp.userservice.handlers.NotFoundException
import com.scrumdapp.userservice.handlers.ServerFaultException
import com.scrumdapp.userservice.repositories.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository, private val groupRequestService: GroupRequestService){

    fun getById(id: Long): UserResponseDto {
        val user = userRepository.findUserById(id) ?: throw NotFoundException(message = "User not found")
        return user.toResponseDto()
    }

    fun getPartialById(id: Long): PartialUserResponseDto {
        val user = userRepository.findAllById(id)
        return user.first().toResponseDto()
    }

    fun getPartialByIds(ids: List<Long>): List<PartialUserResponseDto> {
        val users: MutableList<PartialUserResponseDto> = mutableListOf()
        for (id in ids) {
            val user = userRepository.findAllById(id)
            if (user.isEmpty()) throw NotFoundException(message = "User with id ${id} not found")
            users.add(user.first().toResponseDto())
        }
        return users
    }

    fun generatePassport(id: Long): PassportDto {
        val user = userRepository.findUserById(id) ?: throw NotFoundException(message = "User not found")
        val userGroups = fetchUserGroups(user.id)
        return PassportDto(
            userId = user.id,
            userGroups = userGroups,
            roles = listOf(user.role.name)
        )
    }

    fun upsertUser(dto: UserUpsertDto): UserResponseDto {
        val user = userRepository.findDistinctByEmail(dto.email)
        if (user == null) {
            return userRepository.save(dto.toEntity()).toResponseDto()
        } else {
            user.patchFromDto(dto)
            return userRepository.save(user).toResponseDto()
        }
    }

    fun patchUser(dto: UserPatchDto, userId: Long): UserResponseDto {
        val user = userRepository.findUserById(userId) ?: throw NotFoundException(message = "User with id $userId not found")
        user.patchFromDto(dto)
        return userRepository.save(user).toResponseDto()
    }

    private fun fetchUserGroups(userId: Long): List<Long> {
        val jwt = SecurityContextHolder.getContext().authentication?.principal as? Jwt
            ?: throw ServerFaultException(message = "Auth principal couldn't be found or isn't a valid jwt. To prevent the endpoint is protected.")
        return groupRequestService.fetchGroups(jwt, userId)
    }

}