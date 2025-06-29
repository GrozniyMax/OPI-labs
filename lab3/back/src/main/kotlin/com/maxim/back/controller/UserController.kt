package com.maxim.back.controller

import com.maxim.back.dto.UserDto
import com.maxim.back.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @PostMapping("/loginUser")
    fun loginUser(@RequestBody userDto: UserDto) =
        userService.loginUser(userDto)

    @PostMapping("/signUpUser")
    fun signUpUser(@RequestBody userDto: UserDto) =
        userService.signUpUser(userDto)
}
