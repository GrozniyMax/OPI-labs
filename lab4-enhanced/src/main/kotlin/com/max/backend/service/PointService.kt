package com.max.backend.service

import com.max.backend.dto.PointUserDto
import com.max.backend.dto.UserDto
import com.max.backend.metrics.HitsCounter
import com.max.backend.model.Point
import com.max.backend.repository.PointRepository
import com.max.backend.repository.UserRepository

import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class PointService(
    private val pointRepository: PointRepository,
    private val userRepository: UserRepository,
    private val hitsCounter: HitsCounter
) {

    fun getAllPoints(userDto: UserDto): ResponseEntity<List<Point>> {
        val user =
            userRepository.findByLogin(userDto.login) ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        if (user.password != userDto.password) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
        val points = pointRepository.findAllByLogin(user.login)
        return ResponseEntity.ok(points)
    }

    fun addPoint(pointUserDto: PointUserDto): ResponseEntity<Point> {
        hitsCounter.update(pointUserDto.result)
        val user =
            userRepository.findByLogin(pointUserDto.login) ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        if (user.password != pointUserDto.password) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
        val point = Point(
            login = pointUserDto.login,
            x = pointUserDto.x,
            y = pointUserDto.y,
            r = pointUserDto.r,
            result = pointUserDto.result,
            time = pointUserDto.time
        )
        return ResponseEntity.ok(pointRepository.save(point))
    }

    @Transactional
    fun clearAllPoints(userDto: UserDto): ResponseEntity<List<Point>> {
        val user =
            userRepository.findByLogin(userDto.login) ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        if (user.password != userDto.password) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
        return ResponseEntity.ok(pointRepository.deleteAllByLogin(userDto.login))
    }

}
