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

        var upperCaseCondition =
            PasswordCondition(false, ErrorMessages.UPPER_CASE_CONDITION)
        var lowerCaseCondition =
            PasswordCondition(false, ErrorMessages.LOWER_CASE_CONDITION)
        var digitsCondition = PasswordCondition(false, ErrorMessages.DIGIT_CONDITION)
        var specialCharactersCondition =
            PasswordCondition(false, ErrorMessages.SPECIAL_CHARACTER_CONDITION)

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