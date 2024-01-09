package com.etag.stsyn.data.repository_impl

import com.etag.stsyn.data.model.User
import com.etag.stsyn.data.repository.LoginRepository

class LoginRepositoryImpl : LoginRepository {
    override suspend fun login(user: User, correctPassword: String): Boolean {
        // handle api login here
        return user.password == correctPassword
    }

    override suspend fun getUserByRfidId(rfidId: String): User {
        return User("Kyaw Gyi", "09342342232", "1234", "")
    }
}