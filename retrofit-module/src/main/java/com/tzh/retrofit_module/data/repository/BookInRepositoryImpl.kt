package com.tzh.retrofit_module.data.repository

import com.tzh.retrofit_module.data.local_storage.LocalDataStore
import com.tzh.retrofit_module.data.model.book_in.SaveBookInRequest
import com.tzh.retrofit_module.data.network.ApiResponseHandler
import com.tzh.retrofit_module.data.network.ApiService
import com.tzh.retrofit_module.data.settings.AppConfiguration
import com.tzh.retrofit_module.domain.model.bookIn.BookInResponse
import com.tzh.retrofit_module.domain.model.bookIn.CheckUSCaseResponse
import com.tzh.retrofit_module.domain.model.bookIn.GetAllItemsOfBoxResponse
import com.tzh.retrofit_module.domain.model.bookIn.GetItemsCountNotInBox
import com.tzh.retrofit_module.domain.model.bookIn.SelectBoxForBookInResponse
import com.tzh.retrofit_module.domain.model.login.NormalResponse
import com.tzh.retrofit_module.domain.repository.BookInRepository
import com.tzh.retrofit_module.util.ApiResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BookInRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    localDataStore: LocalDataStore,
    appConfiguration: AppConfiguration
) : BookInRepository {
        private val userFlow = localDataStore.getUser
        private val settingsFlow = appConfiguration.appConfig

    override suspend fun getItemsCountNotInBox(
        boxName: String
    ): ApiResponse<GetItemsCountNotInBox> {
        val userId = userFlow.map { it.userId }.first()

        return ApiResponseHandler.processResponse {
            apiService.getItemsCountNotInBox(boxName, userId)
        }
    }

    override suspend fun getBookInItems(): ApiResponse<BookInResponse> {
        val settings = settingsFlow.first()
        val userId = userFlow.first().userId

        return ApiResponseHandler.processResponse {
            apiService.getBookInItems(
                store = settings.store.name, csNo = settings.csNo, userID = userId
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
        box: String, status: String
    ): ApiResponse<GetAllItemsOfBoxResponse> {
        val loginUserId = userFlow.first().userId
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