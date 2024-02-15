package com.tzh.retrofit_module.domain.model.accountabilityCheck

data class GetAllFilterOptionsResponse(
    val isSuccess: Boolean,
    val error: String?,
    val dropdownSet: DropdownSet
)
