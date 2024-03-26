package com.tzh.retrofit_module.data.network

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

object BaseUrlValidator {

    fun validate(url: String, onComplete: (String) -> Unit) {
        when (true) {
            url.isEmpty() -> onComplete("Base URL must not be empty")
            !url.endsWith("/") -> onComplete("Base URL must end with \''/'\'")
            (!url.startsWith("http://") && !url.startsWith("https://")) -> onComplete("Base URL must start with in update 'http://' or 'https://'.")
            else -> validateUrl(url=url, onComplete = onComplete)
        }
    }
    private fun validateUrl(url: String, onComplete: (String) -> Unit) {
        val client = OkHttpClient.Builder().hostnameVerifier { _, _ -> true }.build()
        val request = Request.Builder()
            .url(url + "HealthCheck")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) onComplete("") else onComplete("Invalid url")
            }

            override fun onFailure(call: Call, e: IOException) {
                onComplete("Invalid Url")
            }
        })
    }
}