package com.tzh.retrofit_module.data.repository_impl

import com.tzh.retrofit_module.data.model.book_in.SaveBookInRequest
import com.tzh.retrofit_module.data.network.ApiService
import com.tzh.retrofit_module.data.repository.BookInRepository
import com.tzh.retrofit_module.domain.model.bookIn.BookInResponse
import com.tzh.retrofit_module.util.ApiResponse
import javax.inject.Inject

class BookInRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) : BookInRepository {
    override suspend fun getBookInItems(
        store: String,
        csNo: String,
        userId: String,
        token: String
    ): ApiResponse<BookInResponse> {
        try {
            val response = apiService.getBookInItems(
                store = store,
                csNo = csNo,
                userID = userId,
                token = "Bearer $token"
            )

            if (response.isSuccess) return ApiResponse.Success(response)
            else return ApiResponse.Error(response.error ?: "")
        } catch (e: Exception) {
            e.printStackTrace()
            return ApiResponse.Error(e.message.toString())
        }
    }

    override suspend fun saveBookIn(token: String, saveBookInRequest: SaveBookInRequest) =
        apiService.saveBookIn(token, saveBookInRequest)
}