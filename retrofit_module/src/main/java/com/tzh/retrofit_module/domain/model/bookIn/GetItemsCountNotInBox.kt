package com.tzh.retrofit_module.domain.model.bookIn

data class GetItemsCountNotInBox(
    val error: Any,
    val isSuccess: Boolean,
    val itemCount: Int
)