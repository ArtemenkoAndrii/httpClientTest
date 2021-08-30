package com.example.springboot

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RequestConfig {
    @Bean
    fun getHost(): Host = Host("http://localhost:8181/")
    @Bean
    fun getResource(): Resource = Resource("/wait/{ms}")
}

class Host(val host: String)
class Resource(val resource: String)
