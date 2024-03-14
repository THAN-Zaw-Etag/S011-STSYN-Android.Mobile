package com.tzh.retrofit_module.data.settings

data class AppConfigModel(
    val handheldReaderId: String = "1",
    val handheldReaderSerialNo: String = "1",
    val apiUrl: String = "",
    val power: Int = 100,
    val store: StoreType = StoreType.DCS,
    val csNo: String = "",
    val needLocation: Boolean = false
)

enum class StoreType { DCS, CS }
