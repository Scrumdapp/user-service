package com.scrumdapp.userservice.users

import com.scrumdapp.userservice.users.dtos.UserUpsertDto
import com.scrumdapp.userservice.users.dtos.UserPatchDto
import com.scrumdapp.userservice.users.dtos.UserResponseDto
import com.scrumdapp.userservice.users.dtos.patchFromDto
import com.scrumdapp.userservice.users.dtos.toEntity
import com.scrumdapp.userservice.users.dtos.toResponseDto
import com.scrumdapp.userservice.groupRequests.GroupRequestService
import com.scrumdapp.userservice.exceptions.NoAccessException
import com.scrumdapp.userservice.exceptions.NotFoundException
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val groupRequestService: GroupRequestService
){

    fun getSelf(id: Long): UserResponseDto {
        val user = userRepository.findUserById(id) ?: throw NotFoundException(message = "User not found")
        return user.toResponseDto()
    }

    fun getById(userId: Long, ownId: Long): UserResponseDto {
        hasMutualGroup(ownId, userId)
        val user = userRepository.findUserById(userId) ?: throw NotFoundException(message = "User not found")
        return user.toResponseDto()
    }

    fun upsert(dto: UserUpsertDto): UserResponseDto {
        val user = userRepository.findDistinctByEmail(dto.email)
        if (user == null) {
            return userRepository.save(dto.toEntity()).toResponseDto()
        } else {
            user.patchFromDto(dto)
            return userRepository.save(user).toResponseDto()
        }
    }

    fun patch(dto: UserPatchDto, userId: Long): UserResponseDto {
        val user = userRepository.findUserById(userId) ?: throw NotFoundException(message = "User with id $userId not found")
        user.patchFromDto(dto)
        return userRepository.save(user).toResponseDto()
    }

    private fun hasMutualGroup(ownId: Long, targetId: Long) {
        val ownGroups = groupRequestService.getUserGroups(ownId)
        val targetGroups = groupRequestService.getUserGroups(targetId)

        val hasCommon = ownGroups.any { it in targetGroups }
        if (!hasCommon) {
            throw NoAccessException(message = "No access to this user")
        }
    }
}

