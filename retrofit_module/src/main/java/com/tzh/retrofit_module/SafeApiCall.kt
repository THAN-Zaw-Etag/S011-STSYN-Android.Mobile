package com.tzh.retrofit_module

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

interface SafeApiCall {

    suspend fun <T> safeApiCall(api: suspend () -> T): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(api.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> {
                        Resource.Failure(
                            false,
                            throwable.code(),
                            throwable.response()?.errorBody()?.string().toString()
                        )
                    }

//                    is NullPointerException -> {
//                        Resource.Failure(false, 204, "Null")
//                    }

                    is SocketTimeoutException -> {
                        Resource.Failure(false, null, throwable.message.toString())
                    }

                    is IOException -> {
                        Resource.Failure(false, null, throwable.message.toString())
                    }

                    else -> {
                        Resource.Failure(true, null, throwable.toString())
                    }
                }
            }
        }
    }


}