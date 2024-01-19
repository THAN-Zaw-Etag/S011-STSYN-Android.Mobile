package com.tzh.retrofit_module.data.model.book_in

data class ItemMovementLog(
    val approverId: Int,
    val buddyId: String,
    val calDate: String,
    val date: String,
    val description: String,
    val handheldReaderId: Int,
    val iS_ONSITE_TRANSFER: String,
    val issuerId: Int,
    val itemId: Int,
    val itemStatus: String,
    val itemType: String,
    val receiverId: Int,
    val receiverName: String,
    val remarks: String,
    val workLoc: String
)