package com.tzh.retrofit_module.domain.model.bookOut

import com.tzh.retrofit_module.domain.model.bookIn.BoxItem

data class OnSiteVerificationResponse(
    val error: String?,
    val isSuccess: Boolean,
    val items: List<BoxItem>
)
