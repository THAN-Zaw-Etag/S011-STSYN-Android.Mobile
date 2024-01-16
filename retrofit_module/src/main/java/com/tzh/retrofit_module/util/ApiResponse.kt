package com.tzh.retrofit_module.util

sealed class ApiResponse<out T> {

    object Loading : ApiResponse<Nothing>()
    object Default : ApiResponse<Nothing>()
    data class Success<T>(val data: T?) : ApiResponse<T>()
    data class Error(val message: String) : ApiResponse<Nothing>()
}