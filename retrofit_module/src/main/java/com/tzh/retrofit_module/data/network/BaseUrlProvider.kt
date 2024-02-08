package com.tzh.retrofit_module.data.network

import android.util.Log
import com.tzh.retrofit_module.data.settings.AppConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class BaseUrlProvider @Inject constructor(
    private val appConfig: AppConfiguration
) {

    private var baseUrl: String = "https://example.com"

    init {
        CoroutineScope(Dispatchers.IO).launch {
            appConfig.appConfig.collect { appConfigModel ->
                Log.d("BaseUrlProvider", "From Class appConfigModel: ${appConfigModel.apiUrl}")
                baseUrl = appConfigModel.apiUrl
            }
        }
    }

    fun getBaseUrl(): String {
        if (baseUrl.isEmpty()) {
            throw IllegalStateException("Base URL is not set.")
        }
        if (!baseUrl.startsWith("http://") && !baseUrl.startsWith("https://")) {
            throw IllegalStateException("Base URL must start with in get 'http://' or 'https://'. Current base URL: $baseUrl")
        }
        return baseUrl
    }
    // todo delete it if not needed
//    fun getBaseUrlWithTrailingSlash(): String {
//        return getBaseUrl().trimEnd('/') + "/"}

    fun updateBaseUrl(newBaseUrl: String) {
        Log.d("BaseUrlProvider", "From Class newBaseUrl: $newBaseUrl")
        if (!newBaseUrl.startsWith("http://") && !newBaseUrl.startsWith("https://")) {
            throw IllegalArgumentException("Base URL must start with in update 'http://' or 'https://'. Provided URL: $newBaseUrl")
        }
        baseUrl = newBaseUrl
    }
}