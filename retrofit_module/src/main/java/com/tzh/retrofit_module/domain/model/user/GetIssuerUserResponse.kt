package com.tzh.retrofit_module.domain.model.user

data class GetIssuerUserResponse(
    val error: String?,
    val isSuccess: Boolean,
    val userModel: UserModel
)