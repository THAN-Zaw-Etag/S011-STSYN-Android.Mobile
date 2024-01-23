package com.tzh.retrofit_module.data.repository

import com.tzh.retrofit_module.data.local_storage.LocalDataStore
import com.tzh.retrofit_module.data.model.login.LoginRequest
import com.tzh.retrofit_module.data.model.login.RefreshTokenRequest
import com.tzh.retrofit_module.data.model.login.UpdatePasswordRequest
import com.tzh.retrofit_module.data.network.ApiService
import com.tzh.retrofit_module.domain.model.bookIn.RefreshTokenResponse
import com.tzh.retrofit_module.domain.model.login.LoginResponse
import com.tzh.retrofit_module.domain.model.login.NormalResponse
import com.tzh.retrofit_module.domain.model.user.UserMenuAccessRightsByIdResponse
import com.tzh.retrofit_module.domain.repository.UserRepository
import com.tzh.retrofit_module.util.AUTHORIZATION_FAILED_ERROR
import com.tzh.retrofit_module.util.AUTHORIZATION_FAILED_MESSAGE
import com.tzh.retrofit_module.util.ApiResponse
import com.tzh.retrofit_module.util.toToken
import kotlinx.coroutines.flow.last
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val localDataStore: LocalDataStore
) : UserRepository {

    override suspend fun login(loginRequest: LoginRequest): ApiResponse<LoginResponse> {
        return try {
            val response = apiService.login(loginRequest)
            if (response.isSuccess) ApiResponse.Success(response)
            else ApiResponse.ApiError(response.error ?: "")
        } catch (e: Exception) {
            e.printStackTrace()
            ApiResponse.ApiError(e.message.toString())
        }
    }

    override suspend fun refreshToken(): ApiResponse<RefreshTokenResponse> {
        return try {
            val user = localDataStore.getUser.last()
            val response = apiService.refreshToken(RefreshTokenRequest(user.token))
            if (response.isSuccess) ApiResponse.Success(response)
            else ApiResponse.ApiError(response.error ?: "")
        } catch (e: Exception) {
            e.printStackTrace()
            ApiResponse.ApiError(e.message.toString())
        }
    }

    override suspend fun updatePassword(
        updatePasswordRequest: UpdatePasswordRequest
    ): ApiResponse<NormalResponse> {
        return try {
            val token = localDataStore.getUser.last().token.toToken()
            val response = apiService.updatePassword(token, updatePasswordRequest)
            if (response.isSuccess) ApiResponse.Success(response)
            else ApiResponse.ApiError(response.error ?: "")
        } catch (e: Exception) {
            e.printStackTrace()
            val error = e.message.toString().trim()
            if (error == AUTHORIZATION_FAILED_ERROR) ApiResponse.AuthorizationError(
                AUTHORIZATION_FAILED_MESSAGE
            )
            else ApiResponse.ApiError(e.message.toString())
        }
    }

    override suspend fun getUserMenuAccessRightsById(): ApiResponse<UserMenuAccessRightsByIdResponse> {
        return try {
            val user = localDataStore.getUser.last()
            val token = user.token.toToken()
            val response = apiService.getUserAccessRightsByRoleId(token = token, id = user.roleId)
            println("isAuthorizationError: $response")
            if (response.isSuccess) ApiResponse.Success(response)
            else ApiResponse.ApiError(response.error ?: "")
        } catch (e: Exception) {
            e.printStackTrace()
            val error = e.message.toString().trim()
            println("isAuthorizationError: $error")
            if (error == AUTHORIZATION_FAILED_ERROR) ApiResponse.AuthorizationError(
                AUTHORIZATION_FAILED_MESSAGE
            )
            else ApiResponse.ApiError(e.message.toString())
        }
    }

    override suspend fun logout() {

    }
}