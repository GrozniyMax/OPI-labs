package com.maxim.back

data class User(val login: String, val password: String, var exists: Boolean) {

    companion object {
        public val users: List<User> = listOf(User(login = "admin", password = "password", exists = false), User(login = "user", password = "password", exists = true))

    }
}
