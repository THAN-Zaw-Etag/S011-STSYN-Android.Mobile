package com.tzh.retrofit_module.data.repository

import com.tzh.retrofit_module.data.local_storage.LocalDataStore
import com.tzh.retrofit_module.data.model.login.LoginRequest
import com.tzh.retrofit_module.data.model.login.RefreshTokenRequest
import com.tzh.retrofit_module.data.model.login.UpdatePasswordRequest
import com.tzh.retrofit_module.data.network.ApiResponseHandler
import com.tzh.retrofit_module.data.network.ApiService
import com.tzh.retrofit_module.domain.model.bookIn.RefreshTokenResponse
import com.tzh.retrofit_module.domain.model.login.LoginResponse
import com.tzh.retrofit_module.domain.model.login.NormalResponse
import com.tzh.retrofit_module.domain.model.user.GetIssuerUserResponse
import com.tzh.retrofit_module.domain.model.user.GetUserByEPCResponse
import com.tzh.retrofit_module.domain.model.user.UserMenuAccessRightsByIdResponse
import com.tzh.retrofit_module.domain.repository.UserRepository
import com.tzh.retrofit_module.util.ApiResponse
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val localDataStore: LocalDataStore
) : UserRepository {
    private val userFlow = localDataStore.getUser

    override suspend fun login(loginRequest: LoginRequest): ApiResponse<LoginResponse> {
        return ApiResponseHandler.processResponse {
            apiService.login(loginRequest)
        }
    }

    override suspend fun refreshToken(): ApiResponse<RefreshTokenResponse> {
        val user = userFlow.first()
        val response = ApiResponseHandler.processResponse {
            apiService.refreshToken(RefreshTokenRequest(user.token))
        }
        when (response) {
            is ApiResponse.Success -> {
                localDataStore.saveToken(response.data!!.token)
            }

            else -> {}
        }
        return response
    }

    override suspend fun updatePassword(
        updatePasswordRequest: UpdatePasswordRequest
    ): ApiResponse<NormalResponse> {
        return ApiResponseHandler.processResponse {
            apiService.updatePassword(updatePasswordRequest)
        }
    }

    override suspend fun getUserMenuAccessRightsById(): ApiResponse<UserMenuAccessRightsByIdResponse> {
        val user = userFlow.first()
        return ApiResponseHandler.processResponse {
            apiService.getUserAccessRightsByRoleId(id = user.roleId)
        }
    }

    override suspend fun saveToken(token: String) {
        localDataStore.saveToken(token)
    }

    override suspend fun getUserByEPC(epc: String): ApiResponse<GetUserByEPCResponse> {
        return ApiResponseHandler.processResponse {
            apiService.getUserByEPC(epc)
        }
    }

    override suspend fun getIssuerByEPC(
        epc: String
    ): ApiResponse<GetIssuerUserResponse> {
        val user = userFlow.first()
        return ApiResponseHandler.processResponse {
            apiService.getIssuerByEPC(epc, user.userId)
        }
    }

    override suspend fun lockUser(nric: String): ApiResponse<NormalResponse> {
        return ApiResponseHandler.processResponse {
            apiService.lockUSer(nric)
        }
    }
}