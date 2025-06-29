package com.maxim.back.dto

data class PointUserDto(
    val login: String,
    val password: String,
    val x: Double,
    val y: Double,
    val r: Double,
    val time: Long,
    val result: Boolean,
)
