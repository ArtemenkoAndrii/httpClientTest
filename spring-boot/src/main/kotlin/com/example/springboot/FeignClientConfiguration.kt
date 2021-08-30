package com.example.springboot

import feign.Feign
import feign.Request
import feign.gson.GsonDecoder
import feign.gson.GsonEncoder
import feign.okhttp.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class FeignClientConfiguration {

    @Bean
    fun client(): WaitAPI {
        return Feign.builder()
            .client(OkHttpClient())
            .encoder(GsonEncoder())
            .decoder(GsonDecoder())
            .options(Request.Options(5000, 5000))
            .target(WaitAPI::class.java, "http://localhost:8181/")
    }
}