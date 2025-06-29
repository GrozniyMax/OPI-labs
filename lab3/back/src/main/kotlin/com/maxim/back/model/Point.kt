package com.maxim.back.model

import jakarta.persistence.*

@Entity
@Table(name = "points")
data class Point(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val login: String = "",

    @Column(nullable = false)
    val x: Double = 0.0,

    @Column(nullable = false)
    val y: Double = 0.0,

    @Column(nullable = false)
    val r: Double = 0.0,

    @Column(nullable = false)
    val time: Long = 0,

    @Column(nullable = false)
    val result: Boolean = false
)
