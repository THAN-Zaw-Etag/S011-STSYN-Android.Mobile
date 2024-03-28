package com.tzh.retrofit_module.data.network

import android.util.Log
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class BaseUrlInterceptor @Inject constructor(private val baseUrlProvider: BaseUrlProvider) :
    Interceptor {
    companion object {
        private const val TAG = "BaseUrlInterceptor"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val originalUrl = request.url

        val newBaseUrlString = baseUrlProvider.getBaseUrl()
        Log.d(TAG, "newBaseUrlString: $newBaseUrlString, originalUrl - ${originalUrl.host}")
        try {
            val newBaseUrl = newBaseUrlString.toHttpUrlOrNull()

            if (newBaseUrl != null) {
                // Remove the trailing slash from the base URL's path if it exists
                val baseWithPath = if (newBaseUrl.encodedPath.endsWith("/")) {
                    newBaseUrl.encodedPath.removeSuffix("/")
                } else {
                    newBaseUrl.encodedPath
                }

                Log.d(TAG, "baseWithPath: $baseWithPath")

                // Remove the leading slash from the original request's path if it exists
                val originalWithPath = if (originalUrl.encodedPath.startsWith("/")) {
                    val segments = originalUrl.pathSegments.toMutableList()
                    segments.removeAt(0)
                    segments.toUrl()
                    //originalUrl.encodedPath.removePrefix("/")
                } else {
                    originalUrl.encodedPath
                }

                // Construct the new URL by combining the base URL with the original request's path and query parameters
                val newUrl = newBaseUrl.newBuilder()
                    .encodedPath("$baseWithPath/$originalWithPath") // Combine paths with a single slash
                    .query(originalUrl.query) // Preserve original query parameters
                    .build()

                // Log the final URL for debugging
                Log.d(TAG, "finalUrl: $newUrl")

                // Create a new request with the updated URL
                val newRequest = request.newBuilder().url(newUrl).build()

                return chain.proceed(newRequest)
            } else {
                return chain.proceed(request) // Proceed with the original request if the base URL is invalid
            }
        } catch (e: Exception){
            return chain.proceed(chain.request())
        }
    }

    private fun List<String>.toUrl(): String {
        var result = ""
        this.map { result += "$it/" }
        return result
    }
}
