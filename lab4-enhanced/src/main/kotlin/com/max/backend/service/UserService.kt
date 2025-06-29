package com.max.backend.service

import com.max.backend.dto.UserDto
import com.max.backend.model.User
import com.max.backend.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun loginUser(userDto: UserDto): ResponseEntity<String> {
        val user =
            userRepository.findByLogin(userDto.login) ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        if (user.password != userDto.password) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
        return ResponseEntity.ok(user.login)
    }

    fun signUpUser(userDto: UserDto): ResponseEntity<String> {
        val exist = userRepository.findByLogin(userDto.login) != null
        if (exist) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
        val user = User(login = userDto.login, password = userDto.password)
        return ResponseEntity.ok(userRepository.save(user).login)
    }
}
