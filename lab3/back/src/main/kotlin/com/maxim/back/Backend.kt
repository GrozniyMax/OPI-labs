package com.maxim.back

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Backend

fun main(args: Array<String>) {
    runApplication<Backend>(*args)
}
