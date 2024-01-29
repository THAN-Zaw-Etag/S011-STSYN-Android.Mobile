package com.tzh.retrofit_module.domain.repository

import com.tzh.retrofit_module.domain.model.bookOut.BookOutResponse
import com.tzh.retrofit_module.util.ApiResponse

interface BookOutRepository {
    suspend fun getAllBookOutItems(): ApiResponse<BookOutResponse>
    suspend fun getAllBookOutBoxes()
}