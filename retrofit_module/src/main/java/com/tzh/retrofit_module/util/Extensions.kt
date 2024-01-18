package com.tzh.retrofit_module.util

fun String.toToken(): String {
    return "Bearer $this"
}