package com.tzh.retrofit_module.domain.model

data class FilterItem(
    val title: String,
    val selectedOption: String,
    val options: List<String> = emptyList()
)

