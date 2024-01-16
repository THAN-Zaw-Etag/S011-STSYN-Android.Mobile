package com.tzh.retrofit_module.domain.model

data class ErrorResponse(
    val error: String,
    val isSuccess: Boolean,
    val rolePermission: Any,
    val token: Any,
    val user: Any
)