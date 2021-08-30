package com.example.springboot

import com.google.gson.JsonObject
import feign.AsyncFeign
import feign.Feign
import feign.gson.GsonDecoder
import feign.okhttp.OkHttpClient
import kotlinx.coroutines.*
import kotlinx.coroutines.future.await
import org.springframework.stereotype.Service
import kotlin.system.measureTimeMillis

@Service
class FeignService(
    private val host: Host) {

    private val syncClient = Feign.builder()
        .client(OkHttpClient())
        .decoder(GsonDecoder())
        .target(WaitAPI::class.java, host.host)

    private val asyncClient = AsyncFeign.asyncBuilder<WaitAPI>()
        .decoder(GsonDecoder())
        .target(WaitAPI::class.java, host.host)

    fun requestSync(n: Int, responseTime: Long) {
        val cnt = mutableSetOf<String>()
        print("Run FeignServiceSync $n times...")

        val time = measureTimeMillis {
            runBlocking {
                (1..n).map {
                    async(Dispatchers.IO) {
                        cnt.add(Thread.currentThread().name)
                        syncClient.waitSync(responseTime)
                    }
                }.awaitAll().size
            }
        }
        println("Completed in ${time}ms")
        println("${cnt.size} of treads were involved")
    }

    fun requestAsync(n: Int, responseTime: Long) {
        val cnt = mutableSetOf<String>()
        print("Run FeignServiceAsync $n times...")

        val time = measureTimeMillis {
            runBlocking {
                (1..n).map {
                    async(Dispatchers.IO) {
                        cnt.add(Thread.currentThread().name)
                        asyncAdapter(responseTime)
                    }
                }.awaitAll().size
            }
        }
        println("Completed in ${time}ms")
        println("${cnt.size} of treads were involved")
    }

    private suspend fun asyncAdapter(ms: Long): JsonObject {
        return asyncClient.waitAsync(ms).await()
    }

}