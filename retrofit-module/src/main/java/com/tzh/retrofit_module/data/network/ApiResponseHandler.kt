package com.tzh.retrofit_module.data.network

import com.tzh.retrofit_module.data.exception.NetworkException
import com.tzh.retrofit_module.domain.model.login.NormalResponse
import com.tzh.retrofit_module.util.AUTHORIZATION_FAILED_MESSAGE
import com.tzh.retrofit_module.util.ApiResponse
import com.tzh.retrofit_module.util.UNKNOWN_ERROR
import kotlinx.coroutines.delay
import org.json.JSONException
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

object ApiResponseHandler {
    suspend fun <T> processResponse(makeApiCall: suspend () -> Response<T>): ApiResponse<T> {
        return try {
            val response = makeApiCall()
            delay(1000)

            if (response.isSuccessful) {
                val normalResponse = getNormalResponseFromApiResponse(response.body().toString())
                val normalResponseError = normalResponse.error ?: UNKNOWN_ERROR
                return if (normalResponse.isSuccess && normalResponseError.contains("null")){
                    ApiResponse.Success(response.body())
                }else{
                    ApiResponse.ApiError(normalResponse.error ?: UNKNOWN_ERROR)
                }
            } else {
                handleResponseCode(response.code())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ApiResponse.ApiError(e.localizedMessage ?: UNKNOWN_ERROR)
            handleError(e)
        }
    }

    private fun getNormalResponseFromApiResponse(response: String): NormalResponse {
        val isSuccess = response.contains("isSuccess=true")
        val pattern = Regex("error=(.*?),")
        val matchResult = pattern.find(response)
        val error = matchResult?.groupValues?.getOrNull(1)
        return NormalResponse(isSuccess = isSuccess, error = error)
    }

    private fun <T> handleError(exception: Exception): ApiResponse<T> {
        return when (exception) {
            is NetworkException -> ApiResponse.ApiError(exception.message.toString())
            is IOException -> ApiResponse.ApiError("Network error occurred")
            is JSONException -> ApiResponse.ApiError("Error parsing response")
            is HttpException -> ApiResponse.ApiError(exception.message.toString())
            is IllegalStateException -> ApiResponse.ApiError(exception.message.toString())
            else -> ApiResponse.ApiError("Unknown error occurred")
        }
    }

    private fun <T> handleResponseCode(code: Int): ApiResponse<T> {
        return when (code) {
            401 -> ApiResponse.AuthorizationError(AUTHORIZATION_FAILED_MESSAGE)
            400 -> ApiResponse.ApiError("Bad request")
            404 -> ApiResponse.ApiError("Resource not found")
            500 -> ApiResponse.ApiError("Internal server error")
            else -> ApiResponse.ApiError("HTTP error: $code")
        }
    }
}