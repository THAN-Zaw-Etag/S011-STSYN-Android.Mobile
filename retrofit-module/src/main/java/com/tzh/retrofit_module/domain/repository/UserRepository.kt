package com.tzh.retrofit_module.domain.repository

import com.tzh.retrofit_module.data.model.login.LoginRequest
import com.tzh.retrofit_module.data.model.login.UpdatePasswordRequest
import com.tzh.retrofit_module.domain.model.bookIn.RefreshTokenResponse
import com.tzh.retrofit_module.domain.model.login.LoginResponse
import com.tzh.retrofit_module.domain.model.login.NormalResponse
import com.tzh.retrofit_module.domain.model.user.GetIssuerUserResponse
import com.tzh.retrofit_module.domain.model.user.GetUserByEPCResponse
import com.tzh.retrofit_module.domain.model.user.UserMenuAccessRightsByIdResponse
import com.tzh.retrofit_module.util.ApiResponse

interface UserRepository {
    suspend fun login(loginRequest: LoginRequest): ApiResponse<LoginResponse>

    suspend fun refreshToken(): ApiResponse<RefreshTokenResponse>
    suspend fun updatePassword(
        updatePasswordRequest: UpdatePasswordRequest
    ): ApiResponse<NormalResponse>

    suspend fun getUserMenuAccessRightsById(): ApiResponse<UserMenuAccessRightsByIdResponse>

    suspend fun saveToken(token: String)

    suspend fun getUserByEPC(epc: String): ApiResponse<GetUserByEPCResponse>

    suspend fun getIssuerByEPC(epc: String): ApiResponse<GetIssuerUserResponse>
}