package com.tzh.retrofit_module.util

const val BASE_URL = "https://18.139.63.32/SMS-STSYN-Dev/"
const val LOGIN_ROUTE = "api/Authenticate/Login"
const val HEALTH_CHECK = "api/Authenticate/HealthCheck"
const val REFRESH_TOKEN_ROUTE = "api/Authenticate/RefreshToken"
const val BOOK_IN_GET_ITEM_ROUTE = "api/BookInOut/GetValidBookInItems"
const val SAVE_BOOK_IN_ROUTE = "api/BookInOut/SaveBookInOut"
const val SELECT_BOX_FOR_BOOK_IN_ROUTE = "api/BookInOut/SelectBoxForBookIn"
const val CHECK_U_S_CASE_BY_BOX_ROUTE = "api/BookInOut/CheckUSCaseByBox"
const val GET_ALL_BOOK_IN_ITEMS_OF_BOX_ROUTE = "api/BookInOut/SelectItemsInCTKBoxForBookIn"
const val GET_ITEMS_COUNT_NOT_IN_BOX = "api/BookInOut/SelectItemsNotInCTKBox"

const val GET_ALL_BOOK_OUT_ITEMS_ROUTE = "api/BookInOut/SelectItemWhereIn"
const val GET_ALL_BOOK_OUT_BOXES_ROUTE = "api/BookInOut/SelectBoxForBookOut"
const val GET_ALL_ITEMS_IN_BOOK_OUT_BOX_ROUTE = "api/BookInOut/SelectItemsInCTKBoxForBookOut"
const val GET_ALL_ITEMS_IN_BOX = "api/BookInOut/SelectItemsInCTKBoxForBookOut"
const val SAVE_ONSITE_CHECK_IN_OUT_IN_ROUTE = "api/BookInOut/SaveOnsiteCheckInOut"

/******************************** ACCOUNTABILITY CHECK *****************************************/
const val SAVE_ACCOUNTABILITY_CHECK_ROUTE = "api/AccountabilityCheck/SaveAccountabilityCheck"
const val GET_ALL_ACCOUNTABILITY_CHECK_ITEMS_ROUTE = "api/AccountabilityCheck/GetOutstandingItems"
const val GET_ALL_FILTER_OPTIONS_ROUTE = "api/AccountabilityCheck/GetFilterOptions"

const val UPDATE_PASSWORD_ROUTE = "api/User/ChangePassword"
const val GET_USER_ACCESS_RIGHTS_BY_ROLE_ID_PATH = "api/User/GetUserAccessRightByRoleId"

const val AUTHORIZATION_FAILED_MESSAGE = "Your authorization token has been expired. Try login again!"

const val UNKNOWN_ERROR = "Unknown Error"

const val GET_USER_BY_EPC_ROUTE = "api/User/GetUserByEPC"
const val GET_ISSUER_BY_EPC = "api/User/GetUserNotIssuerByEPC"
const val SELECT_ONSITE_VERIFY_ITEMS = "api/BookInOut/SelectItemWhereAllNotIn"

/************* ONSITE CHECK IN OUT ******************/
const val GET_ITEMS_FOR_ONSITE_ROUTE = "api/BookInOut/SelectItemForOnsite"

const val SAVE_ONSITE_VERIFICATION = "api/BookInOut/SaveOnsiteVerification"
const val LOCK_USER = "api/Authenticate/LockUser"