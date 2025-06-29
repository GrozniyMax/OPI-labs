package com.maxim.back.controller

import com.maxim.back.dto.PointUserDto
import com.maxim.back.dto.UserDto
import com.maxim.back.model.Point
import com.maxim.back.service.PointService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/points")
class PointController( private val pointService: PointService) {
    @GetMapping("/hello")
    fun getHello() = "Hello"

    @PostMapping("/getAll")
    fun getAllPoints(@RequestBody userDto: UserDto) =
        pointService.getAllPoints(userDto)

    @PostMapping("/addPoint")
    fun addPoint(@RequestBody pointUserDto: PointUserDto) =
        pointService.addPoint(pointUserDto = pointUserDto)

    @PostMapping("/clearAll")
    fun clearAllPoints(@RequestBody userDto: UserDto): ResponseEntity<List<Point>> =
        pointService.clearAllPoints(userDto)
}
