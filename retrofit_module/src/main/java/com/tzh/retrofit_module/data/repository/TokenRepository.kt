package com.tzh.retrofit_module.data.repository

import com.tzh.retrofit_module.data.local_storage.LocalDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class TokenRepository @Inject constructor( private val localDataStore: LocalDataStore) {

    private var token: String? = null
    init {
        // Initialize a coroutine to listen to DataStore changes
        CoroutineScope(Dispatchers.IO).launch {
            localDataStore.getUser.collect { user ->
                token = user.token
            }
        }
    }

    fun getToken(): String? = token
}