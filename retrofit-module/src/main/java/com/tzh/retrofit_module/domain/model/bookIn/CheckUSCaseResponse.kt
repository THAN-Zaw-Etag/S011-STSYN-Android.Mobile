package com.tzh.retrofit_module.domain.model.bookIn

data class CheckUSCaseResponse(
    val error: String?,
    val isSuccess: Boolean,
    val isUsCase: Boolean
)