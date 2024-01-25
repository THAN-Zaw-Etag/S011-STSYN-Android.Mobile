package com.etag.stsyn.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.tzh.retrofit_module.domain.repository.UserRepository
import com.tzh.retrofit_module.util.ApiResponse
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class TokenRefreshWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val userRepository: UserRepository
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result  {
        Log.d("TAG", "doWork: working...")

        return try {
            val response = userRepository.refreshToken()
            when (response) {
                is ApiResponse.Success -> {
                    Log.d("TAG", "doWork: success")
                    userRepository.saveToken(response.data!!.token)
                    return Result.success()
                }

                else -> {
                    Log.d("TAG", "doWork: failed...")
                    return Result.failure()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}