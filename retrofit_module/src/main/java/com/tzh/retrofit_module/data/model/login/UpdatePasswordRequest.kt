package com.tzh.retrofit_module.data.model.login

data class UpdatePasswordRequest(
    val newPassword: String,
    val oldPassword: String,
    val userId: String
)