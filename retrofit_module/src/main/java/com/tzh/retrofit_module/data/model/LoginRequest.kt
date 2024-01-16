package com.tzh.retrofit_module.data.model

data class LoginRequest(
    val id: String,
    val isFromMobile: Boolean,
    val nric: String,
    val password: String,
    val rfid: String
)