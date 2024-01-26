package com.tzh.retrofit_module.data.network

import com.tzh.retrofit_module.data.model.book_in.SaveBookInRequest
import com.tzh.retrofit_module.data.model.login.LoginRequest
import com.tzh.retrofit_module.data.model.login.RefreshTokenRequest
import com.tzh.retrofit_module.data.model.login.UpdatePasswordRequest
import com.tzh.retrofit_module.domain.model.bookIn.BookInResponse
import com.tzh.retrofit_module.domain.model.bookIn.CheckUSCaseResponse
import com.tzh.retrofit_module.domain.model.bookIn.GetAllBookInItemsOfBoxResponse
import com.tzh.retrofit_module.domain.model.bookIn.RefreshTokenResponse
import com.tzh.retrofit_module.domain.model.bookIn.SelectBoxForBookInResponse
import com.tzh.retrofit_module.domain.model.login.LoginResponse
import com.tzh.retrofit_module.domain.model.login.NormalResponse
import com.tzh.retrofit_module.domain.model.user.GetUserByEPCResponse
import com.tzh.retrofit_module.domain.model.user.UserMenuAccessRightsByIdResponse
import com.tzh.retrofit_module.util.BOOK_IN_GET_ITEM_ROUTE
import com.tzh.retrofit_module.util.CHECK_U_S_CASE_BY_BOX_ROUTE
import com.tzh.retrofit_module.util.GET_ALL_BOOK_IN_ITEMS_OF_BOX_ROUTE
import com.tzh.retrofit_module.util.GET_USER_ACCESS_RIGHTS_BY_ROLE_ID_PATH
import com.tzh.retrofit_module.util.GET_USER_BY_EPC_ROUTE
import com.tzh.retrofit_module.util.LOGIN_ROUTE
import com.tzh.retrofit_module.util.REFRESH_TOKEN_ROUTE
import com.tzh.retrofit_module.util.SAVE_BOOK_IN_ROUTE
import com.tzh.retrofit_module.util.SELECT_BOX_FOR_BOOK_IN_ROUTE
import com.tzh.retrofit_module.util.UPDATE_PASSWORD_ROUTE
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

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
    ): Response<GetAllBookInItemsOfBoxResponse>



    @GET(CHECK_U_S_CASE_BY_BOX_ROUTE)
    suspend fun checkUSCaseByBox(
        @Query("Box") box: String
    ): Response<CheckUSCaseResponse>


    @GET(GET_USER_BY_EPC_ROUTE)
    suspend fun getUserByEPC(
        @Query("epc") epc: String
    ): Response<GetUserByEPCResponse>

}