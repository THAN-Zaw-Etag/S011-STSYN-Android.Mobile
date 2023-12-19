package com.tzh.retrofit_module

import com.tzh.retrofit_module.data.model.HealthCheckResponse
import retrofit2.http.GET

interface BaseApiInterface {
    @GET("HealthCheck")
    suspend fun getHealthCheck(): HealthCheckResponse
}
