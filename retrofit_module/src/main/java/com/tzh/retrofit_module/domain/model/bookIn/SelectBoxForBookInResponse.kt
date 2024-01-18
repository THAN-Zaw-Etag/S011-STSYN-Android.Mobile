package com.tzh.retrofit_module.domain.model.bookIn

data class SelectBoxForBookInResponse(
    val error: String?,
    val isSuccess: Boolean,
    val items: List<BoxItem>
)