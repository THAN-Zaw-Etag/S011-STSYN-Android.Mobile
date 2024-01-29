package com.tzh.retrofit_module.domain.model.bookOut

import com.tzh.retrofit_module.domain.model.bookIn.BookInItem

data class BookOutResponse(
    val error: String?,
    val isSuccess: Boolean,
    val items: List<BookInItem>
)
