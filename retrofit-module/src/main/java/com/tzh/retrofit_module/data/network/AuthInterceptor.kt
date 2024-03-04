package com.tzh.retrofit_module.data.network

import com.tzh.retrofit_module.data.repository.TokenRepository
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
            val token = tokenRepository.token
            val newRequest = token?.let {
                originalRequest.newBuilder()
                    .header("Authorization", "Bearer $it")
                    .build()
            } ?: originalRequest

            chain.proceed(newRequest)
        }



    }


}