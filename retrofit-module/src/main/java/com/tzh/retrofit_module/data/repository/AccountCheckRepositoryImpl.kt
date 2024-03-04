package com.tzh.retrofit_module.data.repository

import com.tzh.retrofit_module.data.local_storage.LocalDataStore
import com.tzh.retrofit_module.data.model.account_check.AccountCheckOutstandingItemsRequest
import com.tzh.retrofit_module.data.model.account_check.SaveAccountabilityCheckRequest
import com.tzh.retrofit_module.data.network.ApiResponseHandler
import com.tzh.retrofit_module.data.network.ApiService
import com.tzh.retrofit_module.domain.model.accountabilityCheck.GetAllAccountabilityCheckItemsResponse
import com.tzh.retrofit_module.domain.model.accountabilityCheck.GetAllFilterOptionsResponse
import com.tzh.retrofit_module.domain.model.login.NormalResponse
import com.tzh.retrofit_module.domain.repository.AccountCheckRepository
import com.tzh.retrofit_module.util.ApiResponse
import javax.inject.Inject

class AccountCheckRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    localDataStore: LocalDataStore,
    ): AccountCheckRepository {
    val userFlow = localDataStore.getUser


    override suspend fun getAllFilterOptions(): ApiResponse<GetAllFilterOptionsResponse> {
        return ApiResponseHandler.processResponse {
            apiService.getAllFilterOptions()
        }
    }

    override suspend fun getAllAccountabilityCheckItems(accountCheckOutstandingItemsRequest: AccountCheckOutstandingItemsRequest): ApiResponse<GetAllAccountabilityCheckItemsResponse> {
        return ApiResponseHandler.processResponse {
            apiService.getAllAccountabilityCheckItems(accountCheckOutstandingItemsRequest)
        }
    }
    override suspend fun saveAccountabilityCheck(saveAccountabilityCheckRequest: SaveAccountabilityCheckRequest): ApiResponse<NormalResponse> {
        return ApiResponseHandler.processResponse {
            apiService.saveAccountabilityCheck(saveAccountabilityCheckRequest)
        }
    }
}