package com.tzh.retrofit_module.data.network

import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class BaseUrlInterceptor @Inject constructor(private val baseUrlProvider: BaseUrlProvider) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val originalUrl = request.url

        val newBaseUrlString = baseUrlProvider.getBaseUrl()
        val newBaseUrl = newBaseUrlString.toHttpUrlOrNull()

        return if (newBaseUrl != null) {
            val baseWithPath = if (newBaseUrl.encodedPath.endsWith("/")) {
                newBaseUrl.encodedPath.removeSuffix("/")
            } else {
                newBaseUrl.encodedPath
            }

            // Remove the leading slash from the original request's path if it exists
            val originalWithPath = if (originalUrl.encodedPath.startsWith("/")) {
                originalUrl.encodedPath.removePrefix("/")
            } else {
                originalUrl.encodedPath
            }
            val newUrl = newBaseUrl.newBuilder()
                .encodedPath("$baseWithPath/$originalWithPath") // Combine paths with a single slash
                .query(originalUrl.query) // Preserve original query parameters
                .build()
            val newRequest = request.newBuilder().url(newUrl).build()
            chain.proceed(newRequest)
        } else {
            chain.proceed(request) // Proceed with the original request if the base URL is invalid
        }
    }
}
