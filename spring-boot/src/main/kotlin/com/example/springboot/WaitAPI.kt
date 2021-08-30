package com.example.springboot

import com.google.gson.JsonObject
import feign.HeaderMap
import feign.Param
import feign.RequestLine
import java.util.concurrent.CompletableFuture

interface WaitAPI {
    @RequestLine("GET /wait/{ms}")
    fun waitSync(@Param("ms") responseTime: Long): JsonObject

    @RequestLine("GET /wait/{ms}")
    fun waitAsync(@Param("ms") responseTime: Long): CompletableFuture<JsonObject>

}