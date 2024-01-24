package com.etag.stsyn.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.tzh.retrofit_module.data.model.login.RefreshTokenRequest
import com.tzh.retrofit_module.data.network.ApiService
import com.tzh.retrofit_module.domain.repository.UserRepository
import com.tzh.retrofit_module.util.ApiResponse
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltWorker
class TokenRefreshWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val userRepository: UserRepository,
) : CoroutineWorker(context, params) {
    @Inject
    lateinit var apiService: ApiService
    override suspend fun doWork(): Result  {
        Log.d("TAG", "doWork: working...")
        //userRepository.refreshToken()
        return try {
            Log.d("TAG", "doWork: try")
            val response = apiService.refreshToken(RefreshTokenRequest("hel"))
            Log.d("TAG", "doWork: $response")
            Result.success()
            /*when (response) {
                is ApiResponse.Success -> {
                    Log.d("TAG", "doWork: ${response.data}")
                    return Result.success()
                }

                else -> {
                    Log.d("TAG", "doWork: failed...")
                    return Result.failure()
                }
            }*/
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}