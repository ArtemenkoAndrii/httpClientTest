package com.example.springboot

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController(val feignService: FeignService,
                     val ktorService: KtorService,
                     val httpClientService: HttpClientService,
                     val okHTTPService: OkHTTPService) {

    @GetMapping("/feign_sync")
    fun feignSync(@RequestParam(name = "delay") delay: Long,
             @RequestParam(name = "n") n: Int): String {
        feignService.requestSync(n, delay)
        return "Done"
    }

    @GetMapping("/feign_async")
    fun feignAsync(@RequestParam(name = "delay") delay: Long,
                  @RequestParam(name = "n") n: Int): String {
        feignService.requestAsync(n, delay)
        return "Done"
    }

    @GetMapping("/ktor")
    fun ktor(@RequestParam(name = "delay") delay: Long,
                   @RequestParam(name = "n") n: Int): String {
        ktorService.request(n, delay)
        return "Done"
    }

    @GetMapping("/hc")
    fun hc(@RequestParam(name = "delay") delay: Long,
             @RequestParam(name = "n") n: Int): String {
        httpClientService.request(n, delay)
        return "Done"
    }

    @GetMapping("/okHttp")
    fun okHttp(@RequestParam(name = "delay") delay: Long,
           @RequestParam(name = "n") n: Int): String {
        okHTTPService.request(n, delay)
        return "Done"
    }

}