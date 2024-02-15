package com.tzh.retrofit_module.domain.model.onsiteCheckInOut

import com.tzh.retrofit_module.domain.model.bookIn.BoxItem

data class GetItemsForOnsiteResponse(
    val isSuccess: Boolean,
    val error: String? = null,
    val items: List<BoxItem>
)
