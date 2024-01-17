package com.tzh.retrofit_module.data.network

import com.tzh.retrofit_module.data.model.book_in.SaveBookInRequest
import com.tzh.retrofit_module.data.model.login.LoginRequest
import com.tzh.retrofit_module.data.model.login.UpdatePasswordRequest
import com.tzh.retrofit_module.domain.model.bookIn.BookInResponse
import com.tzh.retrofit_module.domain.model.login.LoginResponse
import com.tzh.retrofit_module.domain.model.login.UpdatePasswordResponse
import com.tzh.retrofit_module.util.BOOK_IN_GET_ITEM_ROUTE
import com.tzh.retrofit_module.util.LOGIN_ROUTE
import com.tzh.retrofit_module.util.SAVE_BOOK_IN_ROUTE
import com.tzh.retrofit_module.util.UPDATE_PASSWORD_ROUTE
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST(LOGIN_ROUTE)
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @GET(BOOK_IN_GET_ITEM_ROUTE)
    suspend fun getBookInItems(
        @Header("Authorization") token: String,
        @Query("Store") store: String,
        @Query("CsNo") csNo: String,
        @Query("LoginUserId") userID: String
    ): BookInResponse

    @POST(SAVE_BOOK_IN_ROUTE)
    suspend fun saveBookIn(
        @Header("Authorization") token: String,
        @Body saveBookInRequest: SaveBookInRequest
    )

    @POST(UPDATE_PASSWORD_ROUTE)
    suspend fun updatePassword(
        @Header("Authorization") token: String,
        @Body updatePasswordRequest: UpdatePasswordRequest
    ): UpdatePasswordResponse

    /*@GET(GET_USER_ACCESS_RIGHTS_BY_ROLE_ID_PATH)
    suspend fun getUserAccessRightsByRoleId(
        @Header("Authorization")
    )*/
}