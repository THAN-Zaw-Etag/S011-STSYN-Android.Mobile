package com.tzh.retrofit_module.domain.model.login

data class CheckStatus(
    val endTime: Any,
    val id: Int,
    val isAdhoc: Boolean,
    val isCompleted: Boolean,
    val isEnd: Boolean,
    val isOutstandingOverwrite: Boolean,
    val isProcessOverwrite: Boolean,
    val isProgress: Boolean,
    val isStart: Boolean,
    val isStartTransaction: Boolean,
    val startTime: String
)