package com.tzh.retrofit_module.domain.repository.CheckIn

import com.tzh.retrofit_module.domain.model.onsiteCheckInOut.GetItemsForOnsiteResponse
import com.tzh.retrofit_module.domain.model.user.GetUserByEPCResponse
import com.tzh.retrofit_module.util.ApiResponse

interface CheckInOutRepository {
    suspend fun getItemsForOnSite(): ApiResponse<GetItemsForOnsiteResponse>
    suspend fun getReceiverByEpc(epc: String): ApiResponse<GetUserByEPCResponse>
}