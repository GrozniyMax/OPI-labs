package com.max.backend.metrics

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class MissesResetFilter(private val hitsCounter: HitsCounter): OncePerRequestFilter() {


    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        filterChain.doFilter(request, response)

        if (request.requestURI == "/actuator/prometheus"){
            println("Set misses counter to 0")
            hitsCounter.dropMissesCounter()
        }


    }
}
