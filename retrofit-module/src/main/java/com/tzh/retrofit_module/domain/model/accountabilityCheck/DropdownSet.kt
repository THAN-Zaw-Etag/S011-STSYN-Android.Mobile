package com.tzh.retrofit_module.domain.model.accountabilityCheck

data class DropdownSet(
    val storeType: List<String> = emptyList(),
    val csNo: List<String> = emptyList(),
    val unitSqn: List<String> = emptyList(),
    val flight: List<String> = emptyList(),
    val itemType: List<String> = emptyList(),
    val itemLocation: List<String> = emptyList(),
    val box: List<String> = emptyList(),
    val remarks: List<String> = emptyList(),
)
