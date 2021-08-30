package localhost

import io.ktor.application.install
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import localhost.plugins.*

fun main() {
    embeddedServer(Netty, port = 8181) {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
            }
        }

        configureRouting()
    }.start(wait = true)
}




