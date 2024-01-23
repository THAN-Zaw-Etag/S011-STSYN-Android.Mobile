package com.tzh.retrofit_module.util

const val BASE_URL = "https://18.139.63.32/SMS-STSYN-Dev/api/"
const val LOGIN_ROUTE = "Authenticate/Login"
const val REFRESH_TOKEN_ROUTE = "Authenticate/RefreshToken"
const val BOOK_IN_GET_ITEM_ROUTE = "BookIn/GetValidBookInItems"
const val SAVE_BOOK_IN_ROUTE = "BookIn/SaveBookIn"
const val SELECT_BOX_FOR_BOOK_IN_ROUTE = "BookIn/SelectBoxForBookIn"
const val CHECK_U_S_CASE_BY_BOX_ROUTE = "BookIn/CheckUSCaseByBox"
const val GET_ALL_BOOK_IN_ITEMS_OF_BOX_ROUTE = "BookIn/SelectItemsInCTKBoxForBookIn"


const val UPDATE_PASSWORD_ROUTE = "User/ChangePassword"
const val GET_USER_ACCESS_RIGHTS_BY_ROLE_ID_PATH = "User/GetUserAccessRightByRoleId"

const val AUTHORIZATION_FAILED_ERROR = "HTTP 401"
const val AUTHORIZATION_FAILED_ERROR_CODE = 401

const val AUTHORIZATION_FAILED_MESSAGE =
    "Your authorization token has been expired. Try login again!"
const val AUTHORIZATION_FAILED_MESSAGE = "Your account has been logged in from another device!"
const val GET_USER_BY_EPC_ROUTE = "User/GetUserByEPC"