package com.etag.stsyn.data.repository

import com.etag.stsyn.data.model.User

interface LoginRepository {
    suspend fun getUserByRfidId(rfidId: String): User
    suspend fun login(
        user: User,
        correctPassword: String
    ): Boolean  // return boolean for testing to check passwords
}