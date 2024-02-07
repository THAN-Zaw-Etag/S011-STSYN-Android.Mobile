package com.tzh.retrofit_module.data.model.account_check

data class StockTake(
    val chkStatusId: String,
    val date: String,
    val handheldReaderId: String,
    val isDone: Boolean,
    val isStockTake: Boolean,
    val itemId: String,
    val shift: String,
    val userId: String
)