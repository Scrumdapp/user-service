package com.scrumdapp.userservice.repositories

import com.scrumdapp.userservice.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Long> {

    fun findByDiscordId(discordId: Long): List<User>
}