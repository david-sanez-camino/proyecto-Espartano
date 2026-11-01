package com.espartano.app.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.espartano.app.EspartanoApp
import com.espartano.app.MainActivity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, EspartanoApp.CANAL_RECORDATORIO_ID)
            .setSmallIcon(android.R.drawable.ic_menu_compass)
            .setContentTitle("ESPARTANO te llama")
            .setContentText("Es hora de registrar tus mediciones. Los espartanos no fallan.")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Es hora de registrar tus mediciones. Los espartanos no fallan. ¿Qué muestran los números hoy?"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val manager = applicationContext.getSystemService(NotificationManager::class.java)
        manager.notify(1001, notification)

        return Result.success()
    }
}
