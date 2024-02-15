package com.tzh.retrofit_module.domain.model.bookOut

import com.tzh.retrofit_module.domain.model.bookIn.BookInItem
import com.tzh.retrofit_module.domain.model.bookIn.BoxItem

data class BookOutResponse(
    val error: String?,
    val isSuccess: Boolean,
    val items: List<BoxItem>
)
