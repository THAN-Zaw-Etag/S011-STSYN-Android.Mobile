package com.tzh.retrofit_module.domain.model.login

import com.tzh.retrofit_module.domain.model.user.UserModel

data class LoginResponse(
    val isSuccess: Boolean,
    val token: String?,
    val error: String?,
    val user: UserModel?,
    val rolePermission: RolePermission?,
    val checkStatus: CheckStatus?
)
