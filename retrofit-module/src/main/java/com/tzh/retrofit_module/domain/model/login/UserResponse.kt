package com.tzh.retrofit_module.domain.model.login

import com.tzh.retrofit_module.data.model.LocalUser

data class UserResponse(
    val airbase: Any,
    val airbaseId: Any,
    val altPhNo: Any,
    val contactNo: Any,
    val flight: String?,
    val flightId: String?,
    val isDeleted: Boolean,
    val nric: String,
    val password: Any,
    val remark: Any,
    val roleId: String,
    val tagId: Any,
    val unit: Any,
    val unitId: Any,
    val userId: String,
    val userName: String,
    val userRole: Any
)

fun UserResponse?.toLocalUser(token: String?): LocalUser {
    return LocalUser(
        name = this?.userName ?: "",
        userId = this?.userId ?: "",
        roleId = this?.roleId ?: "",
        nric = this?.nric ?: "",
        token = token ?: ""
    )
}