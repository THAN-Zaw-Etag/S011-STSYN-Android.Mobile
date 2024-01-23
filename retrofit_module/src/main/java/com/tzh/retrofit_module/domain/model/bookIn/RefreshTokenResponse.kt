package com.tzh.retrofit_module.domain.model.bookIn

data class RefreshTokenResponse(
    val error: String?,
    val isSuccess: Boolean,
    val token: String
)