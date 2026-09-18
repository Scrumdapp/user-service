package com.scrumdapp.userservice.users

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Long> {

    fun findDistinctByEmail(email: String): User?
    fun findUserById(id: Long): User?

    @Query("""
        SELECT 
            u.id AS "id", 
            u.name AS "name"
        FROM users u 
        WHERE u.id IN :ids
    """, nativeQuery = true)
    fun findBulkByIds(
        @Param("ids") ids: List<Long>,
    ): List<PartialUser>

    fun findAllById(id: Long): List<PartialUser>
}