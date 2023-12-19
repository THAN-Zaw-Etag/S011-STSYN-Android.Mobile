package com.tzh.retrofit_module.data.model

import com.google.gson.annotations.SerializedName

data class HealthCheckResponse(

    @field:SerializedName("isSuccess")
    val isSuccess: Boolean
)
