package com.tzh.retrofit_module.data.repository

import com.tzh.retrofit_module.data.local_storage.LocalDataStore
import com.tzh.retrofit_module.data.model.book_in.SaveBookInRequest
import com.tzh.retrofit_module.data.network.ApiResponseHandler
import com.tzh.retrofit_module.data.network.ApiService
import com.tzh.retrofit_module.data.settings.AppConfiguration
import com.tzh.retrofit_module.domain.model.bookIn.GetAllItemsOfBoxResponse
import com.tzh.retrofit_module.domain.model.bookOut.BookOutResponse
import com.tzh.retrofit_module.domain.model.bookOut.GetAllBookOutBoxesResponse
import com.tzh.retrofit_module.domain.model.login.NormalResponse
import com.tzh.retrofit_module.domain.repository.BookOutRepository
import com.tzh.retrofit_module.util.ApiResponse
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class BookOutRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val localDataStore: LocalDataStore,
    private val appConfiguration: AppConfiguration
) : BookOutRepository {
    override suspend fun getAllBookOutItems(): ApiResponse<BookOutResponse> {
        val settings = appConfiguration.appConfig.first()
        return ApiResponseHandler.processResponse {
            apiService.getAllBookOutItems(store = settings.store.name, csNo = settings.csNo)
        }
    }

    override suspend fun getAllBookOutBoxes(): ApiResponse<GetAllBookOutBoxesResponse> {
        return ApiResponseHandler.processResponse {
            apiService.getAllBookOutBoxes()
        }
    }

    override suspend fun getAllItemsInBookOutBox(box: String): ApiResponse<GetAllItemsOfBoxResponse> {
        return ApiResponseHandler.processResponse {
            apiService.getAllItemsInBookOutBox(box)
        }
    }

    override suspend fun saveBookOutItems(saveBookInRequest: SaveBookInRequest): ApiResponse<NormalResponse> {
        return ApiResponseHandler.processResponse {
            apiService.saveBookIn(saveBookInRequest)
        }
    }
}