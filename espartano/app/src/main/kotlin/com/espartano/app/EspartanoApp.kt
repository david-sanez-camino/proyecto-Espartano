package com.espartano.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EspartanoApp : Application() {

    override fun onCreate() {
        super.onCreate()
        crearCanalNotificaciones()
    }

    private fun crearCanalNotificaciones() {
        val canal = NotificationChannel(
            CANAL_RECORDATORIO_ID,
            "Recordatorios ESPARTANO",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notificaciones para registrar tus mediciones"
        }
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(canal)
    }

    companion object {
        const val CANAL_RECORDATORIO_ID = "espartano_recordatorio"
    }
}
