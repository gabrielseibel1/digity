package com.digity

//todo: Use "common" module to share model with frontend
enum class ApiEndpoint {
    UPLOAD;

    fun path() = when (this) {
        UPLOAD -> ROOT + REL_API + REL_UPLOAD
    }

    companion object {
        const val ROOT = "http://localhost:8080"
        const val REL_API = "/api"
        const val REL_UPLOAD = "/upload"
    }
}