package com.maxim.back.repository

import com.maxim.back.model.Point
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PointRepository : JpaRepository<Point, Long> {
    fun findAllByLogin(login: String): List<Point>
    fun deleteAllByLogin(login: String): List<Point>
}
