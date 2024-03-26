package com.tzh.retrofit_module.data.network

object BaseUrlValidator {

    fun validate(url: String, onValidateUrl: () -> Unit): String {
        return when (true) {
            url.isEmpty() -> "Base URL must not be empty"
            !url.endsWith("/") -> "Base URL must end with \''/'\'"
            (!url.startsWith("http://") && !url.startsWith("https://")) -> "Base URL must start with in update 'http://' or 'https://'."
            else -> {
                onValidateUrl()
                " "
            }
        }
    }
}