package com.tzh.retrofit_module.util

sealed class ApiResponse<out T> {
    object Loading : ApiResponse<Nothing>()
    object Default : ApiResponse<Nothing>()
    data class Success<T>(val data: T?) : ApiResponse<T>()
    data class ApiError(val message: String) : ApiResponse<Nothing>()
    data class AuthorizationError(val message: String) : ApiResponse<Nothing>()
}

