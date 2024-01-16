package com.tzh.retrofit_module.data.repository_impl

import com.tzh.retrofit_module.data.network.ApiService
import com.tzh.retrofit_module.data.repository.BookInRepository
import com.tzh.retrofit_module.domain.model.bookIn.BookInResponse
import com.tzh.retrofit_module.util.ApiResponse

class BookInRepositoryImpl(private val apiService: ApiService) : BookInRepository {
    override suspend fun getBookInItems(
        store: String,
        csNo: String,
        userId: String,
        token: String
    ): ApiResponse<BookInResponse> {
        try {
            val response = apiService.getBookInItems(store, csNo, userId, token)
            if (response.isSuccess) return ApiResponse.Success(response)
            else return ApiResponse.Error(response.error ?: "")
        } catch (e: Exception) {
            return ApiResponse.Error(e.message.toString())
        }
    }
}