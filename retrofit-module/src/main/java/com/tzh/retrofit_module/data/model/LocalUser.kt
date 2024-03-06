package com.tzh.retrofit_module.data.model

data class LocalUser(
    val name: String = "-",
    val userId: String = "-",
    val roleId: String = "",
    val nric: String = "",
    val token: String = "",
    val isAdmin: Boolean = false,
    val isPasswordExpired: Boolean = false,
    val isLoggedIn: Boolean = false,
)
