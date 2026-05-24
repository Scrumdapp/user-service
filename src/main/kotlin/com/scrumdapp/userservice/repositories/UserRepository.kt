package com.scrumdapp.userservice.repositories

import com.scrumdapp.userservice.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface PartialUser {
    val id: Long
    val name: String
}
@Repository
interface UserRepository: JpaRepository<User, Long> {

    fun findDistinctByDiscordId(discordId: Long): User?
    fun findUserById(id: Long): User?
    fun findAllById(id: Long): List<PartialUser>
}