package com.tzh.retrofit_module.data.network

import android.util.Log
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class BaseUrlInterceptor @Inject constructor(private val baseUrlProvider: BaseUrlProvider) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val originalUrl = request.url

        // Log the base URL at the start
        val newBaseUrlString = baseUrlProvider.getBaseUrl()
        Log.d("BaseUrlInterceptor", "start newUrl: $newBaseUrlString")

        val newBaseUrl = newBaseUrlString.toHttpUrlOrNull()
        if (newBaseUrl != null) {
            // Construct the new URL using the new base URL but preserving the original path and query parameters
            val newUrl = originalUrl.newBuilder()
                .scheme(newBaseUrl.scheme)
                .host(newBaseUrl.host)
                .port(newBaseUrl.port)
                .encodedPath(newBaseUrl.encodedPath)  // Use encodedPath to preserve the full path
                // If the base URL might also contain query parameters, add/merge them here
                .build()

            // Log the final URL
            Log.d("BaseUrlInterceptor", "end newUrl: $newUrl")

            // Update the request with the new URL
            request = request.newBuilder().url(newUrl).build()
        } else {
            Log.e("BaseUrlInterceptor", "Invalid new base URL: $newBaseUrlString")
        }

        return chain.proceed(request)
    }
}
