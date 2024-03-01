package com.etag.stsyn.util

object PasswordValidator {

    data class PasswordCondition(
        val isValid: Boolean,
        val message: String
    )

    /**
     * @param password is the entered password
     * @return Any three of true conditions*/
    fun validatePassword(password: String): List<String> {

        //TODO confirm this password length in future
       // if (password.length < 12) return listOf("Password must contain at least 12 characters.")  // error message list size = 0 means valid password

        var upperCaseCondition =
            PasswordCondition(false, "Password must contain at least one uppercase letter.")
        var lowerCaseCondition =
            PasswordCondition(false, "Password must contain at least one lowercase letter.")
        var digitsCondition = PasswordCondition(false, "Password must contain at least one digit.")
        var specialCharactersCondition =
            PasswordCondition(false, "Password must contain at least one special character.")

        password.forEach { char ->
            when {
                char.isUpperCase() -> upperCaseCondition = upperCaseCondition.copy(isValid = true)
                char.isLowerCase() -> lowerCaseCondition = lowerCaseCondition.copy(isValid = true)
                char.isDigit() -> digitsCondition = digitsCondition.copy(isValid = true)
                else -> specialCharactersCondition = specialCharactersCondition.copy(isValid = true)
            }
        }
        val conditions = listOf(
            upperCaseCondition,
            lowerCaseCondition,
            digitsCondition,
            specialCharactersCondition
        )
        val isValidPassword = conditions.filter { it.isValid }.size >= 3
        val messages = conditions.filter { !it.isValid }.map { it.message }

        // if password contains any three conditions, return empty error list else return error list
        return if (isValidPassword) listOf() else messages
    }
}