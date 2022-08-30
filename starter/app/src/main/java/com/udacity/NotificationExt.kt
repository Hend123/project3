package com.udacity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat


private const val NOTIFICATION_ID = 0

fun NotificationManager.sendNotification(intent: Intent, applicationContext: Context) {

    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val builder = NotificationCompat.Builder(
        applicationContext, applicationContext.getString(R.string.notification_channel_id)
    )
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(applicationContext.getString(R.string.complete))
        .setContentIntent(contentPendingIntent)
        .addAction(
            R.drawable.abc_vector_test,
            applicationContext.getString(R.string.result_text),
            contentPendingIntent
        )
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        createNotificationChannel(createChannel(applicationContext))
    }
    notify(NOTIFICATION_ID, builder.build())
}

@RequiresApi(Build.VERSION_CODES.O)
private fun createChannel(applicationContext: Context) =
    NotificationChannel(
        applicationContext.getString(R.string.notification_channel_id),
        applicationContext.getString(R.string.notification_channel_name),
        NotificationManager.IMPORTANCE_DEFAULT
    ).apply {
        description = applicationContext.getString(R.string.notification_description)
        enableVibration(true)
        enableLights(true)
    }
