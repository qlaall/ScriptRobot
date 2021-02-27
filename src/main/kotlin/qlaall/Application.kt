package qlaall

import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.html.*
import kotlinx.html.*
import kotlinx.css.*
import io.ktor.application.*
import io.ktor.response.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable

fun main(args: Array<String>): Unit =
        io.ktor.server.netty.EngineMain.main(args)

/**
 * Please note that you can use any other name instead of *module*.
 * Also note that you can have more then one modules in your application.
 * */
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        header("MyCustomHeader")
        allowCredentials = true
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }
    install(DefaultHeaders) {
        header("X-Engine", "Ktor") // will send this header with each response
    }

    install(ContentNegotiation) {
        json()
    }
    runBlocking {
        repeat(100_000) { // launch a lot of coroutines
            launch {
                delay(5000L)
                print(".")
            }
        }
    }

    print("=======\n")
    print("=======\n")



    routing {
        get("/") {
            call.respondHtml {
                head {
                    title {
                        + "hahaha"
                    }
                }
                body {
                    h1 {
                        +"Hello from ME!"
                    }
                }
            }
        }
    }
    routing {
        get("/json/kotlinx-serialization") {
            call.respond(mapOf("hello" to "world"))
        }
    }
    routing {
        get("/json/kotlinx-serialization-data-class") {
            call.respond(TT("武藏","小次郎"))
        }
    }
    routing {
        get("/styles.css") {
            call.respondCss {
                body {
                    backgroundColor = Color.darkBlue
                    margin(0.px)
                }
                rule("h1.page-title") {
                    color = Color.white
                }
            }
        }
    }
    routing {
        get("/html-dsl") {
            call.respondHtml {
              hello()
            }
        }
    }


}

fun HTML.hello(){
    head {
        link(rel = "stylesheet", href = "/styles.css", type = "text/css")
    }
    body {
        h1(classes = "page-title") {
            +"Hello from Ktor!"
        }
        ul {
            for (n in 1..10) {
                li { +"$n" }
            }
        }
    }
}
suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
    this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}
@Serializable
data class TT(val love:String,val peace:String)