package com.tzh.retrofit_module.data.repository_impl

import com.tzh.retrofit_module.data.model.LoginRequest
import com.tzh.retrofit_module.data.network.ApiService
import com.tzh.retrofit_module.data.repository.LoginRepository
import com.tzh.retrofit_module.domain.model.login.LoginResponse
import com.tzh.retrofit_module.util.ApiResponse
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(private val apiService: ApiService) :
    LoginRepository {

    override suspend fun login(loginRequest: LoginRequest): ApiResponse<LoginResponse> {
        try {
            val response = apiService.login(loginRequest)
            if (response.isSuccess) return ApiResponse.Success(response)
            else return ApiResponse.Error(response.error ?: "")
        } catch (e: Exception) {
            return ApiResponse.Error(e.message.toString())
        }
    }

    override suspend fun logout() {

    }
}