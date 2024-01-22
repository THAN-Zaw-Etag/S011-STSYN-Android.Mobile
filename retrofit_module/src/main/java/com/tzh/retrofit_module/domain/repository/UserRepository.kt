package com.tzh.retrofit_module.domain.repository

import com.tzh.retrofit_module.data.model.login.LoginRequest
import com.tzh.retrofit_module.data.model.login.UpdatePasswordRequest
import com.tzh.retrofit_module.domain.model.login.LoginResponse
import com.tzh.retrofit_module.domain.model.login.NormalResponse
import com.tzh.retrofit_module.domain.model.user.UserMenuAccessRightsByIdResponse
import com.tzh.retrofit_module.util.ApiResponse

interface UserRepository {
    suspend fun login(loginRequest: LoginRequest): ApiResponse<LoginResponse>

    suspend fun refreshToken(token: String)
    suspend fun updatePassword(
        updatePasswordRequest: UpdatePasswordRequest
    ): ApiResponse<NormalResponse>

    suspend fun getUserMenuAccessRightsById(): ApiResponse<UserMenuAccessRightsByIdResponse>

    suspend fun logout()
}