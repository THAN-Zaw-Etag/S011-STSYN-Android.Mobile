package com.tzh.retrofit_module.domain.repository

import com.tzh.retrofit_module.data.model.account_check.AccountCheckOutstandingItemsRequest
import com.tzh.retrofit_module.data.model.account_check.SaveAccountabilityCheckRequest
import com.tzh.retrofit_module.domain.model.accountabilityCheck.GetAllAccountabilityCheckItemsResponse
import com.tzh.retrofit_module.domain.model.login.NormalResponse
import com.tzh.retrofit_module.util.ApiResponse

interface AccountCheckRepository {
    suspend fun getAllAccountabilityCheckItems(accountCheckOutstandingItemsRequest: AccountCheckOutstandingItemsRequest): ApiResponse<GetAllAccountabilityCheckItemsResponse>
    suspend fun saveAccountabilityCheck(saveAccountabilityCheckRequest: SaveAccountabilityCheckRequest): ApiResponse<NormalResponse>
}