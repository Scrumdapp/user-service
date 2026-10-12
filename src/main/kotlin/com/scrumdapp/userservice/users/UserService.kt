package com.scrumdapp.userservice.users

import com.scrumdapp.passportplugin.jwt.PassportContent
import com.scrumdapp.userservice.users.dtos.UserUpsertDto
import com.scrumdapp.userservice.users.dtos.UserPatchDto
import com.scrumdapp.userservice.users.dtos.UserResponseDto
import com.scrumdapp.userservice.users.dtos.patchFromDto
import com.scrumdapp.userservice.users.dtos.toEntity
import com.scrumdapp.userservice.users.dtos.toResponseDto
import com.scrumdapp.userservice.groupRequests.GroupRequestService
import com.scrumdapp.userservice.exceptions.NoAccessException
import com.scrumdapp.userservice.exceptions.NotFoundException
import com.scrumdapp.userservice.exceptions.ServerFaultException
import com.scrumdapp.userservice.users.dtos.PartialUserResponseDto
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val groupRequestService: GroupRequestService
){
    fun getUserById(userId: Long, ownPassport: PassportContent): UserResponseDto {

        if (ownPassport.userId.toLong() != userId) {
            hasMutualGroup(ownPassport, userId)
        }
        val user = userRepository.findUserById(userId) ?: throw NotFoundException(message = "User not found")
        return user.toResponseDto()
    }

    fun getUsersByIds(ids: List<Long>): List<PartialUserResponseDto> {
        return userRepository.findBulkByIds(ids).map { it.toResponseDto() }
    }

    fun getUsersByGroup(groupId: Long): List<PartialUserResponseDto> {
        val userIds = groupRequestService.getUserGroups(groupId)

        return userRepository.findBulkByIds(userIds).map { it.toResponseDto() }
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

    private fun hasMutualGroup(ownPassport: PassportContent, targetId: Long) {
        val ownGroups = ownPassport.userGroups
        val targetGroups = groupRequestService.getUserGroups(targetId)


        if (ownGroups.isNullOrEmpty()) throw ServerFaultException(message = "Groups cannot be null")
        val hasCommon = ownGroups.any { it.toLong() in targetGroups }
        if (!hasCommon) {
            throw NoAccessException(message = "No access to this user")
        }
    }
}

