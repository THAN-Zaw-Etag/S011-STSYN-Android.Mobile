package com.tzh.retrofit_module.data.model.book_in

import com.google.gson.annotations.SerializedName


data class ItemMovementLog(
    val approverId: Int,
    val buddyId: String,
    val calDate: String?,
    val date: String,
    val description: String,
    val handheldReaderId: Int,
    @SerializedName("iS_ONSITE_TRANSFER")
    val isOnSiteTransfer: String,
    val issuerId: Int,
    val itemId: Int,
    val itemStatus: String,
    val itemType: String,
    val receiverId: Int,
    val receiverName: String,
    val remarks: String,
    val workLoc: String
)