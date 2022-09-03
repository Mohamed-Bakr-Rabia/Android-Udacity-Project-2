package com.udacity.asteroidradar

import android.app.Application
import androidx.work.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runInterruptible
import java.util.concurrent.TimeUnit

class AsteroidApplication :Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)
    override fun onCreate() {
        super.onCreate()

        applicationScope.launch {
            setupWork()
        }
    }

    private fun setupWork(){
        val constrains = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .build()

        val request =
            PeriodicWorkRequestBuilder<GetAsteroidWorker>(1,TimeUnit.DAYS)
                .setConstraints(constrains)
                .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            GetAsteroidWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }
}