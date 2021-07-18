package com.xiaxiayige.workmanagerdemo

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UploadLocation(context: Context, val workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        Log.i("aaa", "UploadLocation -> 要上传的地址 = " + inputData.getString("location") ?: "none")
        return Result.success()
    }
}

//支持协程
class UploadLocationForCoroutine(context: Context, val workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            Log.i("aaa", "UploadLocation -> 要上传的地址 = " + inputData.getString("location") ?: "none")
            Result.success()
        }
    }
}