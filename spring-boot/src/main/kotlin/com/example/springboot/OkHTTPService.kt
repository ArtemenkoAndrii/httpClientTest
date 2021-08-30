package com.example.springboot

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.future.await
import kotlinx.coroutines.runBlocking
import okhttp3.*
import org.springframework.stereotype.Service
import java.io.IOException
import java.util.concurrent.CompletableFuture
import kotlin.system.measureTimeMillis


@Service
class OkHTTPService(
    private val host: Host,
    private val resource: Resource) {

    private val client = OkHttpClient()

    fun request(n: Int, responseTime: Long) {
        val cnt = mutableSetOf<String>()
        print("Run OkHTTPService $n times...")

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

        val request = Request.Builder()
            .url(host.host+res)
            .build()

        val future = CallbackFuture()
        client.newCall(request).enqueue(future)

        val responseBody = future.await()?.body()?.string()
        return JsonParser.parseString(responseBody).asJsonObject
    }
}

class CallbackFuture : CompletableFuture<Response?>(), Callback {
    override fun onFailure(call: Call, e: IOException) {
        super.completeExceptionally(e)
    }

    override fun onResponse(call: Call, response: Response) {
        super.complete(response)
    }
}