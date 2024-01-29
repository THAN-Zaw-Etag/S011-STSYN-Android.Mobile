package com.tzh.retrofit_module.data.repository

import com.tzh.retrofit_module.data.local_storage.LocalDataStore
import com.tzh.retrofit_module.data.network.ApiResponseHandler
import com.tzh.retrofit_module.data.network.ApiService
import com.tzh.retrofit_module.data.settings.AppConfiguration
import com.tzh.retrofit_module.domain.model.bookOut.BookOutResponse
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

    override suspend fun getAllBookOutBoxes() {

    }
}