package com.tzh.retrofit_module.data.repository_impl

import com.tzh.retrofit_module.data.model.login.LoginRequest
import com.tzh.retrofit_module.data.model.login.UpdatePasswordRequest
import com.tzh.retrofit_module.data.network.ApiService
import com.tzh.retrofit_module.data.repository.UserRepository
import com.tzh.retrofit_module.domain.model.login.LoginResponse
import com.tzh.retrofit_module.domain.model.login.UpdatePasswordResponse
import com.tzh.retrofit_module.util.ApiResponse
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val apiService: ApiService) :
    UserRepository {

    override suspend fun login(loginRequest: LoginRequest): ApiResponse<LoginResponse> {
        try {
            val response = apiService.login(loginRequest)
            if (response.isSuccess) return ApiResponse.Success(response)
            else return ApiResponse.Error(response.error ?: "")
        } catch (e: Exception) {
            return ApiResponse.Error(e.message.toString())
        }
    }

    override suspend fun updatePassword(
        token: String,
        updatePasswordRequest: UpdatePasswordRequest
    ): ApiResponse<UpdatePasswordResponse> {
        try {
            val response = apiService.updatePassword(token, updatePasswordRequest)
            if (response.isSuccess) return ApiResponse.Success(response)
            else return ApiResponse.Error(response.error ?: "")
        } catch (e: Exception) {
            return ApiResponse.Error(e.message.toString())
        }
    }

    override suspend fun logout() {

    }
}