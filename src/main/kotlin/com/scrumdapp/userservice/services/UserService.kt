package com.scrumdapp.userservice.services

import com.scrumdapp.userservice.repositories.UserRepository
import org.springframework.stereotype.Service

interface UserService {

}

@Service
class UserServiceImpl(private val userRepository: UserRepository) : UserService {


}