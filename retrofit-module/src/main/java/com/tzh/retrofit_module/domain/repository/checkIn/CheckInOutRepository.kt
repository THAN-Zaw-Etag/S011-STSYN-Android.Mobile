package com.tzh.retrofit_module.domain.repository.checkIn

import com.tzh.retrofit_module.data.model.book_in.SaveBookInRequest
import com.tzh.retrofit_module.domain.model.login.NormalResponse
import com.tzh.retrofit_module.domain.model.onsiteCheckInOut.GetItemsForOnsiteResponse
import com.tzh.retrofit_module.domain.model.user.GetUserByEPCResponse
import com.tzh.retrofit_module.util.ApiResponse

interface CheckInOutRepository {
    suspend fun getItemsForOnSite(): ApiResponse<GetItemsForOnsiteResponse>
    suspend fun getReceiverByEpc(epc: String): ApiResponse<GetUserByEPCResponse>
    suspend fun saveOnsiteCheckInOut(bookInRequest: SaveBookInRequest): ApiResponse<NormalResponse>
}