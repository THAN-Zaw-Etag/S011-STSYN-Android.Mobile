package com.etag.stsyn.data.repository_impl

import com.etag.stsyn.data.model.LocalUser
import com.etag.stsyn.data.repository.LoginRepository

class LoginRepositoryImpl : LoginRepository {
    override suspend fun login(user: LocalUser, correctPassword: String): Boolean {
        // handle api login here
        return user.password == correctPassword
    }

    override suspend fun getUserByRfidId(rfidId: String): LocalUser {
        return LocalUser("Kyaw Gyi", "09342342232", "1234", "")
    }

    override suspend fun logOut() {
        // handle log out logic here
    }
}