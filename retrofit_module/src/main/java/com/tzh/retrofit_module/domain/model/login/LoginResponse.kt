package com.tzh.retrofit_module.domain.model.login

data class LoginResponse(
    val isSuccess: Boolean,
    val token: String?,
    val error: String?,
    val user: UserResponse?,
    val rolePermission: RolePermission?,
    val checkStatus: CheckStatus?
)
