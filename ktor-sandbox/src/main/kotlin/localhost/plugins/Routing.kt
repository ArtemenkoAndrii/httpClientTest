package localhost.plugins

import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import kotlinx.coroutines.delay
import localhost.entities.WaitResponse

fun Application.configureRouting() {

    // Starting point for a Ktor app:
    routing {
        get("/wait/{ms}") {
            val responseTime = call.parameters["ms"]
            delay(responseTime?.toLongOrNull() ?: 0)
            call.respond(WaitResponse(status = "success"))
        }
    }
    routing {
    }
}
