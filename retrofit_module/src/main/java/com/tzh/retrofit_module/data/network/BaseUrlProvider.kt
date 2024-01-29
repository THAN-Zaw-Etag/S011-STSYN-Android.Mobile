package com.tzh.retrofit_module.data.network

import android.util.Log
import com.tzh.retrofit_module.data.settings.AppConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class BaseUrlProvider @Inject constructor(
    private val appConfig: AppConfiguration
){

    private var baseUrl:String = "https://example.com"

    init {
        CoroutineScope(Dispatchers.IO).launch {
            appConfig.appConfig.collect { appConfigModel ->
                Log.d("BaseUrlProvider", "From Class appConfigModel: ${appConfigModel.apiUrl}")
                baseUrl = appConfigModel.apiUrl
            }
        }
    }

    fun getBaseUrl(): String {
        return baseUrl
    }
    fun updateBaseUrl(newBaseUrl: String) {
        baseUrl = newBaseUrl
    }

    //Todo delete this later if not needed
//    private var baseUrl: String = "https://"
//
//    fun getBaseUrl(): String {
//        if (baseUrl.isEmpty()) {
//            throw IllegalStateException("Base URL is not set.")
//        }
//        if (!baseUrl.startsWith("http://") && !baseUrl.startsWith("https://")) {
//            throw IllegalStateException("Base URL must start with 'http://' or 'https://'. Current base URL: $baseUrl")
//        }
//        return baseUrl
//    }
//
//    fun updateBaseUrl(newBaseUrl: String) {
//        if (!newBaseUrl.startsWith("http://") && !newBaseUrl.startsWith("https://")) {
//            throw IllegalArgumentException("Base URL must start with 'http://' or 'https://'. Provided URL: $newBaseUrl")
//        }
//        baseUrl = newBaseUrl
//    }
}