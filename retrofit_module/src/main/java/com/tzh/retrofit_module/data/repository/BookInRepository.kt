package com.tzh.retrofit_module.data.repository

import com.tzh.retrofit_module.domain.model.bookIn.BookInResponse
import com.tzh.retrofit_module.util.ApiResponse

interface BookInRepository {
    suspend fun getBookInItems(
        store: String,
        csNo: String,
        userId: String,
        token: String
    ): ApiResponse<BookInResponse>
}