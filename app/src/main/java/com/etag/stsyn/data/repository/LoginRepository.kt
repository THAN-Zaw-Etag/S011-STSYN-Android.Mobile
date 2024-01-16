package com.etag.stsyn.data.repository

import com.etag.stsyn.data.model.LocalUser

interface LoginRepository {
    suspend fun getUserByRfidId(rfidId: String): LocalUser
    suspend fun login(
        user: LocalUser,
        correctPassword: String
    ): Boolean  // return boolean for testing to check passwords

    suspend fun logOut()
}