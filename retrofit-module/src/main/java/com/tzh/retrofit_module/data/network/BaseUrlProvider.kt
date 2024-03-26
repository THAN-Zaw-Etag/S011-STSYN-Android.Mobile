package com.tzh.retrofit_module.data.network

import android.util.Log
import com.tzh.retrofit_module.data.settings.AppConfiguration
import com.tzh.retrofit_module.util.BASE_URL
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

                baseUrl = appConfigModel.apiUrl
            }
        }
    }

    fun getBaseUrl(): String {
        if (baseUrl.isEmpty()) {
            updateBaseUrl(BASE_URL)
        }
        if (!baseUrl.startsWith("http://") && !baseUrl.startsWith("https://")) {
            throw IllegalStateException("Base URL must start with in get 'http://' or 'https://'. Current base URL: $baseUrl")
        }
        return baseUrl
    }

    fun updateBaseUrl(newBaseUrl: String) {
        if (!newBaseUrl.startsWith("http://") && !newBaseUrl.startsWith("https://")) {
            throw IllegalArgumentException("Base URL must start with in update 'http://' or 'https://'. Provided URL: $newBaseUrl")
        }
        baseUrl = newBaseUrl
    }
}