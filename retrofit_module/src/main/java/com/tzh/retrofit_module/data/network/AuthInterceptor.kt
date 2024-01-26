package com.tzh.retrofit_module.data.network

import com.tzh.retrofit_module.data.local_storage.LocalDataStore
import com.tzh.retrofit_module.data.model.LocalUser
import com.tzh.retrofit_module.data.repository.TokenRepository
import com.tzh.retrofit_module.util.toToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val tokenRepository: TokenRepository) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        return if (originalRequest.url.toString().contains("Authenticate/Login")){
            chain.proceed(originalRequest)
        }else{
            // Get the token from the repository
            val token = tokenRepository.getToken()
            val newRequest = token?.let {
                originalRequest.newBuilder()
                    .header("Authorization", "Bearer $it")
                    .build()
            } ?: originalRequest

            chain.proceed(newRequest)
        }



    }


}