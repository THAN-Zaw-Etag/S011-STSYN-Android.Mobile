package com.tzh.retrofit_module.data.network

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class BaseUrlInterceptor @Inject constructor(private val baseUrlProvider: BaseUrlProvider) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val originalUrl = request.url

        val baseUrl = baseUrlProvider.getBaseUrl()
        val newBaseUrl = baseUrl.toHttpUrlOrNull()

        val newUrl: HttpUrl = originalUrl.newBuilder()
            .scheme(newBaseUrl!!.scheme)
            .host(newBaseUrl!!.host)
            .port(newBaseUrl!!.port)
            // Add any additional path segments or query parameters as needed
            .build()

        // Create a new request with the modified URL
        val newRequest: Request = request.newBuilder()
            .url(newUrl)
            .build()

        // Proceed with the modified request
        return chain.proceed(newRequest)
    }
}
