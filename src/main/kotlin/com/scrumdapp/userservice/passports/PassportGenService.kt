package com.scrumdapp.userservice.passports

import com.scrumdapp.userservice.groupRequests.GroupRequestService
import com.scrumdapp.userservice.exceptions.NotFoundException
import com.scrumdapp.userservice.users.UserRepository
import org.springframework.stereotype.Service

@Service
class PassportGenService(
    private val userRepository: UserRepository,
    private val groupService: GroupRequestService
) {

    fun generate(userId: Long): PassportDto {
        val user = userRepository.findUserById(userId) ?: throw NotFoundException(message = "User not found")
        val userGroups = groupService.getUserGroups(user.id)
        return PassportDto(
            userId = user.id,
            userGroups = userGroups,
            roles = listOf(user.role.name)
        )
    }
}