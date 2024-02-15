package com.tzh.retrofit_module.domain.model.bookIn

data class BookInResponse(
    val error: String?,
    val isSuccess: Boolean,
    val items: List<BoxItem>
)