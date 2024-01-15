package com.etag.stsyn.util

object PasswordValidator {

    /**
     * @param password is the entered password
     * @return Any three of true conditions*/
    fun isValidPassword(password: String): Boolean {

        if (password.length < 12) return false

        var containsUpperCase = false
        var containsLowerCase = false
        var containDigits = false
        var containsSpecialCharacters = false

        password.forEach { char ->
            when {
                char.isUpperCase() -> containsUpperCase = true
                char.isLowerCase() -> containsLowerCase = true
                char.isDigit() -> containDigits = true
                else -> containsSpecialCharacters = true
            }
        }
        return listOf(
            containsLowerCase,
            containsUpperCase,
            containDigits,
            containsSpecialCharacters
        ).filter { it }.size >= 3
    }
}