package com.scrumdapp.userservice.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

enum class Roles {
    STUDENT, COACH
}

@Entity
@Table(name="users")
class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long = 0

    @Column(unique = true, nullable = false)
    var discordId: Long = 0

    // Encrypt these values
    @Column(nullable = false)
    var name: String = ""

    var profilePicture: String? = null

    @Enumerated(EnumType.STRING)
    var role: Roles = Roles.STUDENT
}