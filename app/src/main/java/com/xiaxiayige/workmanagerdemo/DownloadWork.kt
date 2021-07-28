package com.xiaxiayige.workmanagerdemo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.Worker
import androidx.work.WorkerParameters


class DownloadWork(val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    private val notificationManager =  context.getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager

    override suspend fun doWork(): Result {
//        val inputUrl = inputData.getString(KEY_INPUT_URL)
//            ?: return Result.failure()
//        val outputFile = inputData.getString(KEY_OUTPUT_FILE_NAME)
//            ?: return Result.failure()
        // Mark the Worker as important
        val progress = "Starting Download"
        setForeground(createForegroundInfo(progress))
//        download(inputUrl, outputFile)
        return Result.success()
    }

    private fun download(inputUrl: String, outputFile: String) {
        // Downloads a file and updates bytes read
        // Calls setForegroundInfo() periodically when it needs to update
        // the ongoing Notification
    }
    // Creates an instance of ForegroundInfo which can be used to update the
    // ongoing notification.
    private fun createForegroundInfo(progress: String): ForegroundInfo {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            val name = "VERBOSE_NOTIFICATION_CHANNEL_NAME"
            val description = "VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("2000", name, importance)
            channel.description = description

            notificationManager.createNotificationChannel(channel)
        }

        // Create the notification
        val notification = NotificationCompat.Builder(context, "2000")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("NOTIFICATION_TITLE")
            .setContentText(progress)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(LongArray(0)).build()

        // Show the notification
//        NotificationManagerCompat.from(context).notify(2000, notification)

        return ForegroundInfo(2000,notification,FOREGROUND_SERVICE_TYPE_LOCATION )
    }

    companion object {
        const val KEY_INPUT_URL = "KEY_INPUT_URL"
        const val KEY_OUTPUT_FILE_NAME = "KEY_OUTPUT_FILE_NAME"
    }

}