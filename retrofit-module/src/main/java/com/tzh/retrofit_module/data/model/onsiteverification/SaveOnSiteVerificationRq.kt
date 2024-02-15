package com.tzh.retrofit_module.data.model.onsiteverification

data class SaveOnSiteVerificationRq(
    val stockTakes: List<StockTake>
)

data class StockTake(
    val chkStatusId: Int,
    val date: String,
    val handheldReaderId: Int,
    val isDone: Boolean,
    val isStockTake: Boolean,
    val itemId: Int,
    val shift: String,
    val userId: Int
)