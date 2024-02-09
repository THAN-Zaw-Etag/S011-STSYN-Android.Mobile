package com.etag.stsyn.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.tzh.retrofit_module.domain.repository.UserRepository
import com.tzh.retrofit_module.util.ApiResponse
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class TokenRefreshWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val userRepository: UserRepository
) : CoroutineWorker(context, params) {

    companion object {
        fun refresh(context: Context) {
            val workConstraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val periodicWork = PeriodicWorkRequest.Builder(TokenRefreshWorker::class.java, 15, TimeUnit.MINUTES)
                .setConstraints(workConstraints)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "api_token_refresh_worker",
                ExistingPeriodicWorkPolicy.REPLACE,
                periodicWork
            )
        }
    }

    override suspend fun doWork(): Result  {
        Log.d("TAG", "doWork: working...")

        return try {
            when (val response = userRepository.refreshToken()) {
                is ApiResponse.Success -> {
                    Log.d("TAG", "doWork: success")

                    val outputData = workDataOf("api_token" to response.data!!.token)
                    Result.success(outputData)
                }

                else -> {
                    Log.d("TAG", "doWork: failed...")
                    Result.failure()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}