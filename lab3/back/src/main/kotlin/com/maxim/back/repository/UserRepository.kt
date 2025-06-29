package com.maxim.back.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<com.maxim.back.model.User, Long> {
    fun findByLogin(login: String): com.maxim.back.model.User?
}
