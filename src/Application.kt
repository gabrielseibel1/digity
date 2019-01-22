package com.digity

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.http.*
import kotlinx.html.*
import kotlinx.css.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.locations.*
import io.ktor.util.KtorExperimentalAPI
import java.io.File
import java.io.IOException

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalLocationsAPI
@Location("/api")
class API {

    @Location("/upload")
    class Upload
}

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(Locations)

    install(ContentNegotiation) { gson {} }

    install(StatusPages) {
        exception<AuthenticationException> { cause ->
            call.respond(HttpStatusCode.Unauthorized)
        }
        exception<AuthorizationException> { cause ->
            call.respond(HttpStatusCode.Forbidden)
        }

    }

    val config = environment.config.config("digity")
    val uploadDirPath = config.property("dirs.upload").getString()
    val uploadDir = File(uploadDirPath)
    if (!uploadDir.mkdirs() && !uploadDir.exists()) {
        throw IOException("Failed to create directory ${uploadDir.absolutePath}")
    }

    routing {

        // root route with frontend
        get("/") {
            call.respondFile(File("index.html"))
        }

        // REST api routes (only one for now)
        upload(uploadDir)

        // static files route
        static("static") {
            files(uploadDirPath)
            files("classes/production/frontend/lib")
            files("classes/production/frontend")
        }

        get("/styles.css") {
            call.respondCss {
                body {
                    backgroundColor = Color.red
                }
                p {
                    fontSize = 2.em
                }
                rule("p.myclass") {
                    color = Color.blue
                }
            }
        }
    }

    MnistClassifier().initTensorFlow()
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()

fun FlowOrMetaDataContent.styleCss(builder: CSSBuilder.() -> Unit) {
    style(type = ContentType.Text.CSS.toString()) {
        +CSSBuilder().apply(builder).toString()
    }
}

fun CommonAttributeGroupFacade.style(builder: CSSBuilder.() -> Unit) {
    this.style = CSSBuilder().apply(builder).toString().trim()
}

suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
    this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}