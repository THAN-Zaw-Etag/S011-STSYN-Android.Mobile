package com.tzh.retrofit_module.data.repository

import com.tzh.retrofit_module.data.local_storage.LocalDataStore
import com.tzh.retrofit_module.data.model.book_in.SaveBookInRequest
import com.tzh.retrofit_module.data.network.ApiResponseHandler
import com.tzh.retrofit_module.data.network.ApiService
import com.tzh.retrofit_module.domain.model.bookIn.BookInResponse
import com.tzh.retrofit_module.domain.model.bookIn.CheckUSCaseResponse
import com.tzh.retrofit_module.domain.model.bookIn.GetAllItemsOfBoxResponse
import com.tzh.retrofit_module.domain.model.bookIn.SelectBoxForBookInResponse
import com.tzh.retrofit_module.domain.model.login.NormalResponse
import com.tzh.retrofit_module.domain.repository.BookInRepository
import com.tzh.retrofit_module.util.ApiResponse
import javax.inject.Inject

class BookInRepositoryImpl @Inject constructor(
    private val apiService: ApiService, private val localDataStore: LocalDataStore
) : BookInRepository {
    override suspend fun getBookInItems(
        store: String, csNo: String, userId: String
    ): ApiResponse<BookInResponse> {
        return ApiResponseHandler.processResponse {
            apiService.getBookInItems(
                store = store, csNo = csNo, userID = userId
            )
        }
    }

    override suspend fun saveBookIn(saveBookInRequest: SaveBookInRequest): ApiResponse<NormalResponse> {
        return ApiResponseHandler.processResponse {
            apiService.saveBookIn( saveBookInRequest)
        }
    }

    override suspend fun getBoxItemsForBookIn(issuerId: String): ApiResponse<SelectBoxForBookInResponse> {
        return ApiResponseHandler.processResponse {
            apiService.getBoxItemsForBookIn( issuerId)
        }
    }

    override suspend fun getAllBookItemsOfBox(
        box: String, status: String, loginUserId: String
    ): ApiResponse<GetAllItemsOfBoxResponse> {
        return ApiResponseHandler.processResponse {
            apiService.getAllBookInItemsOfBox( box, status, loginUserId)
        }
    }

    override suspend fun checkUSCaseByBox(boxName: String): ApiResponse<CheckUSCaseResponse> {
        return ApiResponseHandler.processResponse {
            apiService.checkUSCaseByBox( boxName)
        }
    }
}