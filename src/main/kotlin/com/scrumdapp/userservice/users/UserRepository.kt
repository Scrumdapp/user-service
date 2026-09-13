package com.scrumdapp.userservice.users

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Long> {

    fun findDistinctByEmail(email: String): User?
    fun findUserById(id: Long): User?
    fun findAllById(id: Long): List<PartialUser>
}