package com.tzh.retrofit_module.data.network

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class BaseUrlInterceptor @Inject constructor(private val baseUrlProvider: BaseUrlProvider) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val newBaseUrlString = baseUrlProvider.getBaseUrl()

        // Ensure the new base URL string is not empty and is a valid URL
        if (newBaseUrlString.isNotEmpty()) {
            val newBaseUrl = newBaseUrlString.toHttpUrlOrNull()

            // Use the new base URL if it's valid, otherwise, proceed with the original request URL
            newBaseUrl?.let {
                val newUrl = request.url.newBuilder()
                    .scheme(it.scheme)
                    .host(it.host)
                    .port(it.port)
                    .build()
                request = request.newBuilder()
                    .url(newUrl)
                    .build()
            }
        }

        return chain.proceed(request)
    }
}

