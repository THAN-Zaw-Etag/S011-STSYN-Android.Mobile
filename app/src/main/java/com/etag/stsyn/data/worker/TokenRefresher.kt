package com.etag.stsyn.data.worker

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object TokenRefresher {
    fun refresh(workManager: WorkManager) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED).build()

        val workName = "refreshWorkName"

        workManager.cancelUniqueWork(workName)

        val periodicRefreshRequest =
            PeriodicWorkRequest.Builder(TokenRefreshWorker::class.java, 16, TimeUnit.MINUTES)
                .setInitialDelay(5L, TimeUnit.SECONDS)
                .setConstraints(constraints)
                .build()

        val oneTimeWorkRequest = OneTimeWorkRequestBuilder<TokenRefreshWorker>()
            .setInitialDelay(10, TimeUnit.SECONDS)
            .build()

        workManager.enqueueUniquePeriodicWork(
            workName,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicRefreshRequest
        )

    }
}