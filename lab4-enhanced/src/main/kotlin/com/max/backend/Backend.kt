package com.max.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * Starts application
 */
@SpringBootApplication
class Backend

fun main(args: Array<String>) {

    runApplication<Backend>(*args)
}
