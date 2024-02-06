package com.tzh.retrofit_module.domain.model.accountabilityCheck

import com.tzh.retrofit_module.domain.model.bookIn.BoxItem

data class GetAllAccountabilityCheckItemsResponse(
    val isSuccess: Boolean,
    val error: String?,
    val items: List<BoxItem>
)
