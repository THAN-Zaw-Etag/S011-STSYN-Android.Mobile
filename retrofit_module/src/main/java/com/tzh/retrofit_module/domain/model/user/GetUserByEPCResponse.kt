package com.tzh.retrofit_module.domain.model.user

data class GetUserByEPCResponse(
    val error: String? = null,
    val isSuccess: Boolean = false,
    val userModel: UserModel = UserModel()
)
data class UserModel(
    val airbase: String ="",
    val airbaseId: String = "",
    val altPhNo: String = "",
    val contactNo: String = "",
    val flight: String = "",
    val flightId: String = "",
    val isDeleted: Boolean = false,
    val nric: String ="",
    val password: String = "",
    val remark: String = "",
    val roleId: String = "",
    val tagId: String = "",
    val unit: String = "",
    val unitId: String = "",
    val userId: String = "",
    val userName: String = "",
    val userRole: String =""
)