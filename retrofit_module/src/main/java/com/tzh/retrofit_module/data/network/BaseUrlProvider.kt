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

    private var baseUrl:String = "https://18.139.63.32/SMS-STSYN-Dev/api/"

    init {
        CoroutineScope(Dispatchers.IO).launch {
            appConfig.appConfig.collect { appConfigModel ->
                Log.d("BaseUrlProvider", "appConfigModel: $appConfigModel")
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
}