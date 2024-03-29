package com.tzh.retrofit_module.domain.repository

import com.tzh.retrofit_module.data.model.book_in.SaveBookInRequest
import com.tzh.retrofit_module.domain.model.bookIn.BookInResponse
import com.tzh.retrofit_module.domain.model.bookIn.CheckUSCaseResponse
import com.tzh.retrofit_module.domain.model.bookIn.GetAllItemsOfBoxResponse
import com.tzh.retrofit_module.domain.model.bookIn.GetItemsCountNotInBox
import com.tzh.retrofit_module.domain.model.bookIn.SelectBoxForBookInResponse
import com.tzh.retrofit_module.domain.model.login.NormalResponse
import com.tzh.retrofit_module.util.ApiResponse

interface BookInRepository {
    suspend fun getBookInItems(): ApiResponse<BookInResponse>

    suspend fun saveBookIn(saveBookInRequest: SaveBookInRequest): ApiResponse<NormalResponse>
    suspend fun getBoxItemsForBookIn(issuerId: String): ApiResponse<SelectBoxForBookInResponse>

    suspend fun checkUSCaseByBox(boxName: String): ApiResponse<CheckUSCaseResponse>

    suspend fun getItemsCountNotInBox(boxName: String): ApiResponse<GetItemsCountNotInBox>

    suspend fun getAllBookItemsOfBox(
        box: String,
        status: String
    ): ApiResponse<GetAllItemsOfBoxResponse>

}