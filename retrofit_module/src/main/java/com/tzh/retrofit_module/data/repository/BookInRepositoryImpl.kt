package com.tzh.retrofit_module.data.repository

import com.tzh.retrofit_module.data.local_storage.LocalDataStore
import com.tzh.retrofit_module.data.model.book_in.SaveBookInRequest
import com.tzh.retrofit_module.data.network.ApiService
import com.tzh.retrofit_module.domain.model.bookIn.BookInResponse
import com.tzh.retrofit_module.domain.model.bookIn.CheckUSCaseResponse
import com.tzh.retrofit_module.domain.model.bookIn.GetAllBookInItemsOfBoxResponse
import com.tzh.retrofit_module.domain.model.bookIn.SelectBoxForBookInResponse
import com.tzh.retrofit_module.domain.model.login.NormalResponse
import com.tzh.retrofit_module.domain.repository.BookInRepository
import com.tzh.retrofit_module.util.AUTHORIZATION_FAILED_ERROR_CODE
import com.tzh.retrofit_module.util.AUTHORIZATION_FAILED_MESSAGE
import com.tzh.retrofit_module.util.ApiResponse
import com.tzh.retrofit_module.util.toToken
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class BookInRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val localDataStore: LocalDataStore
) : BookInRepository {
    override suspend fun getBookInItems(
        store: String,
        csNo: String,
        userId: String
    ): ApiResponse<BookInResponse> {
        return try {
            val token = localDataStore.getUser.first().token.toToken()
            val response = apiService.getBookInItems(
                store = store,
                csNo = csNo,
                userID = userId,
                token = token
            )

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

    override suspend fun saveBookIn(saveBookInRequest: SaveBookInRequest): ApiResponse<NormalResponse> {
        return try {
            val user = localDataStore.getUser.first()
            val token = user.token.toToken()
            val response = apiService.saveBookIn(token, saveBookInRequest)

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

    override suspend fun getBoxItemsForBookIn(issuerId: String): ApiResponse<SelectBoxForBookInResponse> {
        return try {
            val user = localDataStore.getUser.first()
            val token = user.token.toToken()
            val response = apiService.getBoxItemsForBookIn(token, issuerId)

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

    override suspend fun getAllBookItemsOfBox(
        box: String,
        status: String,
        loginUserId: String
    ): ApiResponse<GetAllBookInItemsOfBoxResponse> {
        return try {
            val user = localDataStore.getUser.first()
            val token = user.token.toToken()
            val response = apiService.getAllBookInItemsOfBox(token, box, status, loginUserId)

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

    override suspend fun checkUSCaseByBox(boxName: String): ApiResponse<CheckUSCaseResponse> {
        return try {
            val response = apiService.checkUSCaseByBox(boxName)
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
}