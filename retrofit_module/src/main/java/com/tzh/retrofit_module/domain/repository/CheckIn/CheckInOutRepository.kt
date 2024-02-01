package com.tzh.retrofit_module.domain.repository.CheckIn

import com.tzh.retrofit_module.domain.model.onsiteCheckInOut.GetItemsForOnsiteResponse
import com.tzh.retrofit_module.util.ApiResponse

interface CheckInOutRepository {
    suspend fun getItemsForOnSite(): ApiResponse<GetItemsForOnsiteResponse>
}