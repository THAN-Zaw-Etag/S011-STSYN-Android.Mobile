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
import com.tzh.retrofit_module.domain.model.user.GetUserByEPCResponse
import com.tzh.retrofit_module.domain.model.user.UserMenuAccessRightsByIdResponse
import com.tzh.retrofit_module.domain.repository.UserRepository
import com.tzh.retrofit_module.util.ApiResponse
import com.tzh.retrofit_module.util.toToken
import kotlinx.coroutines.flow.last
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val localDataStore: LocalDataStore
) : UserRepository {

    override suspend fun login(loginRequest: LoginRequest): ApiResponse<LoginResponse> {
        return ApiResponseHandler.processResponse {
            apiService.login(loginRequest)
        }
    }

    override suspend fun refreshToken(): ApiResponse<RefreshTokenResponse> {
        val user = localDataStore.getUser.last()
        return ApiResponseHandler.processResponse {
            apiService.refreshToken(RefreshTokenRequest(user.token))
        }
    }

    override suspend fun updatePassword(
        updatePasswordRequest: UpdatePasswordRequest
    ): ApiResponse<NormalResponse> {
        val token = localDataStore.getUser.last().token.toToken()
        return ApiResponseHandler.processResponse {
            apiService.updatePassword(token, updatePasswordRequest)
        }
    }

    override suspend fun getUserMenuAccessRightsById(): ApiResponse<UserMenuAccessRightsByIdResponse> {
        val user = localDataStore.getUser.last()
        val token = user.token.toToken()

        return ApiResponseHandler.processResponse {
            apiService.getUserAccessRightsByRoleId(token = token, id = user.roleId)
        }
    }

    override suspend fun logout() {

    }

    override suspend fun getUserByEPC(epc: String): ApiResponse<GetUserByEPCResponse> {
        return ApiResponseHandler.processResponse {
            apiService.getUserByEPC(epc)
        }
    }
}