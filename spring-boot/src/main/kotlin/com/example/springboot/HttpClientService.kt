package com.example.springboot

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.*
import kotlinx.coroutines.future.await
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.system.measureTimeMillis

@Service
class HttpClientService(
    private val host: Host,
    private val resource: Resource) {

    private val asyncClient = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_1_1)
        .build();

    fun request(n: Int, responseTime: Long) {
        val cnt = mutableSetOf<String>()
        print("Run HttpClientService $n times...")

        val time = measureTimeMillis {
            runBlocking {
                (1..n).map {
                    async(Dispatchers.IO) {
                        cnt.add(Thread.currentThread().name)
                        doRequest(responseTime)
                    }
                }.awaitAll().size
            }
        }
        println("Completed in ${time}ms")
        println("${cnt.size} of treads were involved")
    }

    private suspend fun doRequest(delay: Long): JsonObject {
        val res = resource.resource.replace("{ms}", delay.toString())

        val httpRequest = HttpRequest.newBuilder()
            .uri(URI.create(host.host+res))
            .build()

        val responseBody = asyncClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString()).let {
            it.await().body()
        }

        return JsonParser.parseString(responseBody).asJsonObject
    }
}