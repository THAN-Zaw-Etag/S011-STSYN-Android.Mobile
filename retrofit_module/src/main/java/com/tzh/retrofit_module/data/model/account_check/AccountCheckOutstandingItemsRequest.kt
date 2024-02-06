package com.tzh.retrofit_module.data.model.account_check

data class AccountCheckOutstandingItemsRequest(
    val box: String = "",
    val csNo: Int = 0,
    val flight: String = "",
    val itemLocation: String = "",
    val itemType: String = "",
    val remarks: String = "",
    val storeType: String = "",
    val unitSqn: String = ""
)