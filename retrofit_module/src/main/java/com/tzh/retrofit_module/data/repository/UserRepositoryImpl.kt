package com.tzh.retrofit_module.data.repository

import com.tzh.retrofit_module.data.local_storage.LocalDataStore
import com.tzh.retrofit_module.data.model.login.LoginRequest
import com.tzh.retrofit_module.data.model.login.RefreshTokenRequest
import com.tzh.retrofit_module.data.model.login.UpdatePasswordRequest
import com.tzh.retrofit_module.data.network.ApiService
import com.tzh.retrofit_module.domain.model.bookIn.RefreshTokenResponse
import com.tzh.retrofit_module.domain.model.login.LoginResponse
import com.tzh.retrofit_module.domain.model.login.NormalResponse
import com.tzh.retrofit_module.domain.model.user.GetUserByEPCResponse
import com.tzh.retrofit_module.domain.model.user.UserMenuAccessRightsByIdResponse
import com.tzh.retrofit_module.domain.repository.UserRepository
import com.tzh.retrofit_module.util.AUTHORIZATION_FAILED_ERROR_CODE
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
            if (response.isSuccessful) ApiResponse.Success(response)
            if (response.code() == AUTHORIZATION_FAILED_ERROR_CODE) ApiResponse.AuthorizationError(
                AUTHORIZATION_FAILED_MESSAGE
            )
            else ApiResponse.ApiError(response.body()?.error ?: "")
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
            if (response.isSuccessful) ApiResponse.Success(response)
            if (response.code() == AUTHORIZATION_FAILED_ERROR_CODE) ApiResponse.AuthorizationError(
                AUTHORIZATION_FAILED_MESSAGE
            )
            else ApiResponse.ApiError(response.body()?.error ?: "")

        } catch (e: Exception) {
            e.printStackTrace()
            ApiResponse.ApiError(e.message.toString())
        }
    }

    override suspend fun getUserMenuAccessRightsById(): ApiResponse<UserMenuAccessRightsByIdResponse> {
        return try {
            val user = localDataStore.getUser.last()
            val token = user.token.toToken()

            val response = apiService.getUserAccessRightsByRoleId(token = token, id = user.roleId)
            if (response.isSuccessful) ApiResponse.Success(response)
            if (response.code() == AUTHORIZATION_FAILED_ERROR_CODE) ApiResponse.AuthorizationError(
                AUTHORIZATION_FAILED_MESSAGE
            )
            else ApiResponse.ApiError(response.body()?.error ?: "")
        } catch (e: Exception) {
            e.printStackTrace()
            ApiResponse.ApiError(e.message.toString())
        }
    }

    override suspend fun logout() {

    }

    override suspend fun getUserByEPC(epc: String): ApiResponse<GetUserByEPCResponse> {
        return try {
            val response = apiService.getUserByEPC(epc)
            if (response.isSuccessful && response.body() != null) {
                // response.code() will give you the HTTP status code
                ApiResponse.Success(response.body()!!)
            } else {
                val responseCode = response.code()
                if (responseCode == AUTHORIZATION_FAILED_ERROR_CODE) {
                    ApiResponse.AuthorizationError( AUTHORIZATION_FAILED_MESSAGE)
                } else {
                    ApiResponse.ApiError(response.message() ?: "")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ApiResponse.ApiError(e.localizedMessage ?: "An unknown error occurred")
        }
    }
}