package com.etag.stsyn.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.tzh.retrofit_module.domain.repository.UserRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

@HiltWorker
class TokenRefreshWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val userRepository: UserRepository
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result = coroutineScope {
        Log.d("TAG", "doWork: working...")
        //val response = userRepository.refreshToken()
        //Log.d("TAG", "doWork: $response")
        delay(3000)
        Result.success()
        /*try {
            when (response) {
                is ApiResponse.Success -> {
                    Log.d("TAG", "doWork: ${response.data?.token}")
                     Result.success()
                }

                else -> {
                    Log.d("TAG", "doWork: failed...")
                     Result.failure()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
             Result.failure()
        }*/
    }

}