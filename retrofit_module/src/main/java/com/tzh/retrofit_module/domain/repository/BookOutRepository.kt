package com.tzh.retrofit_module.domain.repository

import com.tzh.retrofit_module.data.model.book_in.SaveBookInRequest
import com.tzh.retrofit_module.domain.model.bookIn.GetAllItemsOfBoxResponse
import com.tzh.retrofit_module.domain.model.bookOut.BookOutResponse
import com.tzh.retrofit_module.domain.model.bookOut.GetAllBookOutBoxesResponse
import com.tzh.retrofit_module.domain.model.bookOut.ItemWhereNotInResponse
import com.tzh.retrofit_module.domain.model.login.NormalResponse
import com.tzh.retrofit_module.util.ApiResponse

interface BookOutRepository {
    suspend fun getAllBookOutItems(): ApiResponse<BookOutResponse>
    suspend fun getAllBookOutBoxes(): ApiResponse<GetAllBookOutBoxesResponse>
    suspend fun getAllItemsInBookOutBox(box: String): ApiResponse<GetAllItemsOfBoxResponse>
    suspend fun saveBookOutItems(saveBookInRequest: SaveBookInRequest): ApiResponse<NormalResponse>
    suspend fun getAllNotInItems(): ApiResponse<ItemWhereNotInResponse>
}