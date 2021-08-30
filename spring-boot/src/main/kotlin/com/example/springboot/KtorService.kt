package com.example.springboot

import com.google.gson.JsonObject
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import kotlinx.coroutines.*
import org.springframework.stereotype.Service
import kotlin.system.measureTimeMillis

@Service
class KtorService(
    private val host: Host,
    private val resource: Resource) {

    private val asyncClient = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
    }

    fun request(n: Int, responseTime: Long) {
        val cnt = mutableSetOf<String>()
        print("Run KtorService $n times...")

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
        return asyncClient.get(host.host+res)
    }

}