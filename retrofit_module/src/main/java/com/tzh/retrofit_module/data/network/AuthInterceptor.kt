package com.tzh.retrofit_module.data.network

import com.tzh.retrofit_module.data.local_storage.LocalDataStore
import com.tzh.retrofit_module.data.model.LocalUser
import com.tzh.retrofit_module.util.toToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val localDataStore: LocalDataStore) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        var user: LocalUser? = null
        CoroutineScope(Dispatchers.IO).launch {
            user = localDataStore.getUser.first()
        }
        val modifiedUrl = originalRequest.url
            .newBuilder()
            .build()

        val modifiedRequest = originalRequest.newBuilder()
            .url(modifiedUrl)
            .addHeader("Authorization", user?.token!!.toToken()) // Add bearer token header
            .build()

        return chain.proceed(modifiedRequest)
    }
}