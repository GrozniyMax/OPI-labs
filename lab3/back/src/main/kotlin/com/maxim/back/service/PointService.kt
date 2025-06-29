package com.maxim.back.service

import com.maxim.back.dto.PointUserDto
import com.maxim.back.dto.UserDto
import com.maxim.back.model.Point
import com.maxim.back.repository.PointRepository
import com.maxim.back.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class PointService(
    private val pointRepository: PointRepository,

    private val userRepository: UserRepository
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
