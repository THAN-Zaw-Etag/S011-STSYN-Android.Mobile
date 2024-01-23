package com.tzh.retrofit_module.domain.model

data class ExpandedScannedItemModel(
    val serialNo: String,
    val description: String,
    val code: String,
    val location: String,
    val storeLocation: String,
    val status: String,
)