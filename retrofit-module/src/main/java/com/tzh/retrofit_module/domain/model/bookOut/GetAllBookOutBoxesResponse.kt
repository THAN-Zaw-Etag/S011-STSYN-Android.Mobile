package com.tzh.retrofit_module.domain.model.bookOut

import com.tzh.retrofit_module.domain.model.bookIn.BoxItem

data class GetAllBookOutBoxesResponse(
    val error: String? = null,
    val isSuccess: Boolean,
    val items: List<BoxItem>
)
