package com.digity

//todo: Use "common" module to share model with frontend
data class ApiResult<T>(val result: T, val success: Boolean = true, val message: String = "")