package com.espartano.app.notifications

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val WORK_NAME = "espartano_recordatorio"
    }

    fun programar(frecuenciaDias: Int) {
        val request = PeriodicWorkRequestBuilder<ReminderWorker>(
            frecuenciaDias.toLong(), TimeUnit.DAYS
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    fun cancelar() {
        WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
    }
}
