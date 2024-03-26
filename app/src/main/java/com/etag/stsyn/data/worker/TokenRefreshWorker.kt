package com.etag.stsyn.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.tzh.retrofit_module.domain.repository.UserRepository
import com.tzh.retrofit_module.util.ApiResponse
import com.tzh.retrofit_module.util.DateUtil
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class TokenRefreshWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
    private val userRepository: UserRepository
) : CoroutineWorker(context, params) {

    companion object {
        private const val WORKER_NAME = "api_token_refresh_worker"
        fun refresh(workManager: WorkManager) {
            val workConstraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val periodicWork =
                PeriodicWorkRequest.Builder(TokenRefreshWorker::class.java, 15, TimeUnit.MINUTES)
                    .setConstraints(workConstraints)
                    .build()

            workManager.enqueueUniquePeriodicWork(
                WORKER_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                periodicWork
            )
        }

        fun cancel(workManager: WorkManager) {
            workManager.cancelUniqueWork(WORKER_NAME)
        }
    }

    override suspend fun doWork(): Result {

        return try {
            when (val response = userRepository.refreshToken()) {
                is ApiResponse.Success -> {
                    Result.success()
                }

                else -> {
                    Result.failure()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}