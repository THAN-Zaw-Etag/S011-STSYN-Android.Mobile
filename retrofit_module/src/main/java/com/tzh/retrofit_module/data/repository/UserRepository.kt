package com.tzh.retrofit_module.data.repository

import com.tzh.retrofit_module.data.model.login.LoginRequest
import com.tzh.retrofit_module.data.model.login.UpdatePasswordRequest
import com.tzh.retrofit_module.domain.model.login.LoginResponse
import com.tzh.retrofit_module.domain.model.login.UpdatePasswordResponse
import com.tzh.retrofit_module.util.ApiResponse

interface UserRepository {
    suspend fun login(loginRequest: LoginRequest): ApiResponse<LoginResponse>
    suspend fun updatePassword(
        token: String,
        updatePasswordRequest: UpdatePasswordRequest
    ): ApiResponse<UpdatePasswordResponse>

    suspend fun logout()
}