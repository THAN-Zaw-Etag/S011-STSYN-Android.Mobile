package com.tzh.retrofit_module.domain.model.bookIn

data class GetAllBookInItemsOfBoxResponse(
    val error: String?,
    val isSuccess: Boolean,
    val items: List<BoxItem>
)