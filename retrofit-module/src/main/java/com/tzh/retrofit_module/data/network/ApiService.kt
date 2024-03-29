package com.tzh.retrofit_module.data.network

import com.tzh.retrofit_module.data.model.account_check.AccountCheckOutstandingItemsRequest
import com.tzh.retrofit_module.data.model.account_check.SaveAccountabilityCheckRequest
import com.tzh.retrofit_module.data.model.book_in.SaveBookInRequest
import com.tzh.retrofit_module.data.model.login.LoginRequest
import com.tzh.retrofit_module.data.model.login.RefreshTokenRequest
import com.tzh.retrofit_module.data.model.login.UpdatePasswordRequest
import com.tzh.retrofit_module.data.model.onsiteverification.SaveOnSiteVerificationRq
import com.tzh.retrofit_module.domain.model.accountabilityCheck.GetAllAccountabilityCheckItemsResponse
import com.tzh.retrofit_module.domain.model.accountabilityCheck.GetAllFilterOptionsResponse
import com.tzh.retrofit_module.domain.model.bookIn.BookInResponse
import com.tzh.retrofit_module.domain.model.bookIn.CheckUSCaseResponse
import com.tzh.retrofit_module.domain.model.bookIn.GetAllItemsOfBoxResponse
import com.tzh.retrofit_module.domain.model.bookIn.GetItemsCountNotInBox
import com.tzh.retrofit_module.domain.model.bookIn.RefreshTokenResponse
import com.tzh.retrofit_module.domain.model.bookIn.SelectBoxForBookInResponse
import com.tzh.retrofit_module.domain.model.bookOut.BookOutResponse
import com.tzh.retrofit_module.domain.model.bookOut.GetAllBookOutBoxesResponse
import com.tzh.retrofit_module.domain.model.bookOut.OnSiteVerificationResponse
import com.tzh.retrofit_module.domain.model.login.LoginResponse
import com.tzh.retrofit_module.domain.model.login.NormalResponse
import com.tzh.retrofit_module.domain.model.onsiteCheckInOut.GetItemsForOnsiteResponse
import com.tzh.retrofit_module.domain.model.user.GetIssuerUserResponse
import com.tzh.retrofit_module.domain.model.user.GetUserByEPCResponse
import com.tzh.retrofit_module.domain.model.user.UserMenuAccessRightsByIdResponse
import com.tzh.retrofit_module.util.BOOK_IN_GET_ITEM_ROUTE
import com.tzh.retrofit_module.util.CHECK_U_S_CASE_BY_BOX_ROUTE
import com.tzh.retrofit_module.util.GET_ALL_ACCOUNTABILITY_CHECK_ITEMS_ROUTE
import com.tzh.retrofit_module.util.GET_ALL_BOOK_IN_ITEMS_OF_BOX_ROUTE
import com.tzh.retrofit_module.util.GET_ALL_BOOK_OUT_BOXES_ROUTE
import com.tzh.retrofit_module.util.GET_ALL_BOOK_OUT_ITEMS_ROUTE
import com.tzh.retrofit_module.util.GET_ALL_FILTER_OPTIONS_ROUTE
import com.tzh.retrofit_module.util.GET_ALL_ITEMS_IN_BOX
import com.tzh.retrofit_module.util.GET_ISSUER_BY_EPC
import com.tzh.retrofit_module.util.GET_ITEMS_COUNT_NOT_IN_BOX
import com.tzh.retrofit_module.util.GET_ITEMS_FOR_ONSITE_ROUTE
import com.tzh.retrofit_module.util.GET_USER_ACCESS_RIGHTS_BY_ROLE_ID_PATH
import com.tzh.retrofit_module.util.GET_USER_BY_EPC_ROUTE
import com.tzh.retrofit_module.util.HEALTH_CHECK
import com.tzh.retrofit_module.util.LOCK_USER
import com.tzh.retrofit_module.util.LOGIN_ROUTE
import com.tzh.retrofit_module.util.REFRESH_TOKEN_ROUTE
import com.tzh.retrofit_module.util.SAVE_ACCOUNTABILITY_CHECK_ROUTE
import com.tzh.retrofit_module.util.SAVE_BOOK_IN_ROUTE
import com.tzh.retrofit_module.util.SAVE_ONSITE_CHECK_IN_OUT_IN_ROUTE
import com.tzh.retrofit_module.util.SAVE_ONSITE_VERIFICATION
import com.tzh.retrofit_module.util.SELECT_BOX_FOR_BOOK_IN_ROUTE
import com.tzh.retrofit_module.util.SELECT_ONSITE_VERIFY_ITEMS
import com.tzh.retrofit_module.util.UPDATE_PASSWORD_ROUTE
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {
    @POST(LOGIN_ROUTE)
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST(REFRESH_TOKEN_ROUTE)
    suspend fun refreshToken(
        @Body refreshTokenRequest: RefreshTokenRequest
    ): Response<RefreshTokenResponse>

    @GET(BOOK_IN_GET_ITEM_ROUTE)
    suspend fun getBookInItems(
        @Query("Store") store: String,
        @Query("CsNo") csNo: String,
        @Query("LoginUserId") userID: String
    ): Response<BookInResponse>

    @POST(SAVE_BOOK_IN_ROUTE)
    suspend fun saveBookIn(
        @Body saveBookInRequest: SaveBookInRequest
    ): Response<NormalResponse>


    @POST(UPDATE_PASSWORD_ROUTE)
    suspend fun updatePassword(
        @Body updatePasswordRequest: UpdatePasswordRequest
    ): Response<NormalResponse>


    @GET(GET_USER_ACCESS_RIGHTS_BY_ROLE_ID_PATH)
    suspend fun getUserAccessRightsByRoleId(
        @Query("roleId") id: String
    ): Response<UserMenuAccessRightsByIdResponse>

    @GET(SELECT_BOX_FOR_BOOK_IN_ROUTE)
    suspend fun getBoxItemsForBookIn(
        @Query("issuerId") issuerId: String
    ): Response<SelectBoxForBookInResponse>


    @GET(GET_ALL_BOOK_IN_ITEMS_OF_BOX_ROUTE)
    suspend fun getAllBookInItemsOfBox(
        @Query("box") box: String,
        @Query("status") status: String,
        @Query("loginUserId") loginUserId: String,
    ): Response<GetAllItemsOfBoxResponse>

    @GET(GET_ITEMS_COUNT_NOT_IN_BOX)
    suspend fun getItemsCountNotInBox(
        @Query("box") box: String,
        @Query("loginUserId") loginUserId: String
    ): Response<GetItemsCountNotInBox>


    /* BOOK OUT*/
    @GET(GET_ALL_BOOK_OUT_ITEMS_ROUTE)
    suspend fun getAllBookOutItems(
        @Query("Store") store: String,
        @Query("CsNo") csNo: String
    ): Response<BookOutResponse>

    @GET(GET_ALL_BOOK_OUT_BOXES_ROUTE)
    suspend fun getAllBookOutBoxes(): Response<GetAllBookOutBoxesResponse>

    @GET(GET_ALL_ITEMS_IN_BOX)
    suspend fun getAllItemsInBookOutBox(@Query("box") box: String): Response<GetAllItemsOfBoxResponse>

    @GET(CHECK_U_S_CASE_BY_BOX_ROUTE)
    suspend fun checkUSCaseByBox(
        @Query("Box") box: String
    ): Response<CheckUSCaseResponse>


    @GET(GET_USER_BY_EPC_ROUTE)
    suspend fun getUserByEPC(
        @Query("epc") epc: String,
    ): Response<GetUserByEPCResponse>

    @GET(GET_ISSUER_BY_EPC)
    suspend fun getIssuerByEPC(
        @Query("epc") epc: String,
        @Query("loginUserId") loginUserId: String,
    ): Response<GetIssuerUserResponse>



    @GET(SELECT_ONSITE_VERIFY_ITEMS)
    suspend fun getOnSiteVerifyItems(
        @Query("Store") store: String,
        @Query("CsNo") csNo: String,
        @Query("LoginUserId") loginUserId: String,
    ): Response<OnSiteVerificationResponse>


    @POST(SAVE_ONSITE_VERIFICATION)
    suspend fun saveOnSiteVerificationItems(
        @Body saveOnSiteVerificationRq: SaveOnSiteVerificationRq
    ): Response<NormalResponse>

    /* ONSITE CHECK IN / OUT */
    @GET(GET_ITEMS_FOR_ONSITE_ROUTE)
    suspend fun getItemsForOnSite(
        @Query("Store") store: String,
        @Query("CsNo") csNo: String
    ): Response<GetItemsForOnsiteResponse>

    @POST(SAVE_ONSITE_CHECK_IN_OUT_IN_ROUTE)
    suspend fun saveOnsiteCheckInOut(
        @Body saveBookInRequest: SaveBookInRequest
    ): Response<NormalResponse>

    /* ACCOUNTABILITY CHECK */

    @POST(GET_ALL_ACCOUNTABILITY_CHECK_ITEMS_ROUTE)
    suspend fun getAllAccountabilityCheckItems(
        @Body accountabilityCheckRequest: AccountCheckOutstandingItemsRequest
    ): Response<GetAllAccountabilityCheckItemsResponse>

    @GET(GET_ALL_FILTER_OPTIONS_ROUTE)
    suspend fun getAllFilterOptions(): Response<GetAllFilterOptionsResponse>

    @POST(SAVE_ACCOUNTABILITY_CHECK_ROUTE)
    suspend fun saveAccountabilityCheck(
        @Body saveAccountabilityCheckRequest: SaveAccountabilityCheckRequest
    ): Response<NormalResponse>

    @POST(LOCK_USER)
    suspend fun lockUSer(
        @Query("nric") nric: String
    ): Response<NormalResponse>

    @GET (HEALTH_CHECK)
    suspend fun validateUrl(): Response<NormalResponse>
}
