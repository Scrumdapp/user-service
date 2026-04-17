package com.scrumdapp.userservice.entities

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.stereotype.Repository

enum class Roles {
    STUDENT, COACH
}

@Entity
@Table(name="users")
class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long = 0

    var discordId: Long? = null

    // Encrypt these values
    var firstName: String? = null
    var lastName: String? = null

    var profilePicture: String? = null

    @Enumerated(EnumType.STRING)
    var role: Roles = Roles.STUDENT
}