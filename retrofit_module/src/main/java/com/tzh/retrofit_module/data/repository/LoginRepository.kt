package com.tzh.retrofit_module.data.repository

import com.tzh.retrofit_module.data.model.LoginRequest
import com.tzh.retrofit_module.domain.model.login.LoginResponse
import com.tzh.retrofit_module.util.ApiResponse

interface LoginRepository {
    suspend fun login(loginRequest: LoginRequest): ApiResponse<LoginResponse>
    suspend fun logout()
}