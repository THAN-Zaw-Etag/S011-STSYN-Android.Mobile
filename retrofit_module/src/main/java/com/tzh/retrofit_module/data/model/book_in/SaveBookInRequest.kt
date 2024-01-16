package com.tzh.retrofit_module.data.model.book_in

data class SaveBookInRequest(
    val itemMovementLogs: List<ItemMovementLog>,
    val printJob: PrintJob
)