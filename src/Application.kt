package com.digity

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.http.*
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
@Location(ApiEndpoint.REL_API)
class API {

    @Location(ApiEndpoint.REL_UPLOAD)
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

    install(CORS) {
        anyHost()
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
            files("frontend/css")
        }
    }

    MnistClassifier().initTensorFlow()
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()