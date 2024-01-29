package com.tzh.retrofit_module.data.network

import android.util.Log
import okhttp3.HttpUrl
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

        if (newBaseUrl != null) {
            // Remove the trailing slash from the base URL's path if it exists
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

            // Construct the new URL by combining the base URL with the original request's path and query parameters
            val newUrl = newBaseUrl.newBuilder()
                .encodedPath("$baseWithPath/$originalWithPath") // Combine paths with a single slash
                .query(originalUrl.query) // Preserve original query parameters
                .build()

            // Log the final URL for debugging
            Log.d("BaseUrlInterceptor", "Final URL: $newUrl")

            // Create a new request with the updated URL
            val newRequest = request.newBuilder().url(newUrl).build()

            return chain.proceed(newRequest)
        } else {
            Log.e("BaseUrlInterceptor", "Invalid base URL: $newBaseUrlString")
            return chain.proceed(request) // Proceed with the original request if the base URL is invalid
        }
    }
}
