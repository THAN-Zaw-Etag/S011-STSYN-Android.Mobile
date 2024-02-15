package com.tzh.retrofit_module.domain.model.user

import com.tzh.retrofit_module.domain.model.login.RolePermission

data class UserMenuAccessRightsByIdResponse(
    val isSuccess: Boolean,
    val error: String? = null,
    val rolePermission: RolePermission
)
