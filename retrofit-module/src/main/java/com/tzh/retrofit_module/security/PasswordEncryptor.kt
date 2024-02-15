package com.tzh.retrofit_module.security

import java.security.MessageDigest
import java.util.Base64

object PasswordEncryptor {
    //TODO hash password not real encryption
    fun encrypt(password: ByteArray): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password)
        return Base64.getEncoder().encodeToString(bytes)
    }
}