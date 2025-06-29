package com.maxim.back.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.CorsFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class CorsConfig {
    @Bean
    fun corsFilter(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration().apply {
            addAllowedOrigin("*") // Разрешить все источники
            addAllowedHeader("*") // Разрешить все заголовки
            addAllowedMethod("*") // Разрешить все методы (GET, POST и т.д.)
        }
        source.registerCorsConfiguration("/**", config)
        return CorsFilter(source)
    }
}
