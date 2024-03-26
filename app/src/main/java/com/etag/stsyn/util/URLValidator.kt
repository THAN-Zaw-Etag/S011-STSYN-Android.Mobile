package com.etag.stsyn.util

object URLValidator {
    fun validate(url: String): String {
        return when (true) {
            url.isEmpty() -> "Base URL must not be empty"
            (!url.startsWith("http://") && !url.startsWith("https://")) -> "Base URL must start with in update 'http://' or 'https://'."
            else -> ""
        }
    }
}