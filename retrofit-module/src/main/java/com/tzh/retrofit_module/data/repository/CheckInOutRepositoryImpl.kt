package com.tzh.retrofit_module.data.repository

import com.tzh.retrofit_module.data.model.book_in.SaveBookInRequest
import com.tzh.retrofit_module.data.network.ApiResponseHandler
import com.tzh.retrofit_module.data.network.ApiService
import com.tzh.retrofit_module.data.settings.AppConfiguration
import com.tzh.retrofit_module.domain.model.login.NormalResponse
import com.tzh.retrofit_module.domain.model.onsiteCheckInOut.GetItemsForOnsiteResponse
import com.tzh.retrofit_module.domain.model.user.GetUserByEPCResponse
import com.tzh.retrofit_module.domain.repository.checkIn.CheckInOutRepository
import com.tzh.retrofit_module.util.ApiResponse
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CheckInOutRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    appConfiguration: AppConfiguration
) : CheckInOutRepository {
    val settings = appConfiguration.appConfig
    override suspend fun getItemsForOnSite(): ApiResponse<GetItemsForOnsiteResponse> {
        val appConfig = settings.first()
        return ApiResponseHandler.processResponse {
            apiService.getItemsForOnSite(store = appConfig.store.name, csNo = appConfig.csNo)
        }
    }

    override suspend fun getReceiverByEpc(epc: String): ApiResponse<GetUserByEPCResponse> {
        return ApiResponseHandler.processResponse {
            apiService.getUserByEPC(epc)
        }
    }

    override suspend fun saveOnsiteCheckInOut(bookInRequest: SaveBookInRequest): ApiResponse<NormalResponse> {
        return ApiResponseHandler.processResponse {
            apiService.saveBookIn(bookInRequest)
        }
    }
}