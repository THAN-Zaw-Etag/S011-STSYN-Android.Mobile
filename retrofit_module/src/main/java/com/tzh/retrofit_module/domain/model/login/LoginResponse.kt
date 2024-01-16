package com.tzh.retrofit_module.domain.model.login

import com.tzh.retrofit_module.domain.model.UserResponse

data class LoginResponse(
    val isSuccess: Boolean,
    val token: String,
    val error: String?,
    val user: UserResponse
)
