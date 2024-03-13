package com.etag.stsyn.util

object ErrorMessages {
    const val SPECIAL_CHARACTER_CONDITION = "Password must contain at least one special character."
    const val DIGIT_CONDITION = "Password must contain at least one digit."
    const val LOWER_CASE_CONDITION = "Password must contain at least one lowercase letter."
    const val UPPER_CASE_CONDITION = "Password must contain at least one uppercase letter."
    const val KEY_IN_LOCATION = "Please Key In Location"
    const val PASSWORD_MUST_NOT_BE_EMPTY = "Password must not be empty."
    const val LOGIN_FAILS = "Login fails. Invalid user ID or password."
    const val ACCOUNT_HAS_BEEN_LOCKED = "Your account has been locked for 10 invalid attempts. Please contact system administrator."
    const val READ_AN_ITEM_FIRST = "Please read an item first."
    const val INCLUDE_OVER_DUE_ITEM = "Include overdue calibration item. Can only book out for calibration."
    const val INCLUDE_TOOLS_OR_PUBS = "Include TOOLs or PUBs, cannot book out as calibration."
    const val READ_A_BOX_FIRST = "Please read a box first."
    const val ITEM_CANNOT_BE_FOUND_IN_THE_LIST = "Item cannot be found in the book in list"
    const val SOMETHING_WENT_WRONG = "Something went wrong! Please try again."
    const val INCLUDE_INVALID_CALIBRATION = "Include invalid calibration date!"
}