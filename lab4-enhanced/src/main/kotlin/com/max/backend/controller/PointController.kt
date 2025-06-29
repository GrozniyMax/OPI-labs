package com.max.backend.controller

import com.max.backend.dto.PointUserDto
import com.max.backend.dto.UserDto
import com.max.backend.metrics.AreaMetric
import com.max.backend.model.Point
import com.max.backend.service.PointService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/points")
class PointController(
    private val pointService: PointService,
    private val areaMetric: AreaMetric) {
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

    @GetMapping("/r")
    fun updateR(@RequestParam r: Double) {
        areaMetric.updateR(r)
    }
}
