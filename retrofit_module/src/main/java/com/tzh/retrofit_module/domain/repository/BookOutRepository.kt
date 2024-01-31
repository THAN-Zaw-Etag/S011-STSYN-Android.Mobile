package com.tzh.retrofit_module.domain.repository

import com.tzh.retrofit_module.data.model.book_in.SaveBookInRequest
import com.tzh.retrofit_module.domain.model.bookOut.BookOutResponse
import com.tzh.retrofit_module.domain.model.bookOut.GetAllBookOutBoxesResponse
import com.tzh.retrofit_module.domain.model.login.NormalResponse
import com.tzh.retrofit_module.util.ApiResponse

interface BookOutRepository {
    suspend fun getAllBookOutItems(): ApiResponse<BookOutResponse>
    suspend fun getAllBookOutBoxes(): ApiResponse<GetAllBookOutBoxesResponse>
    suspend fun saveBookOutItems(saveBookInRequest: SaveBookInRequest): ApiResponse<NormalResponse>
}