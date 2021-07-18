package com.xiaxiayige.workmanagerdemo

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class UploadLocation(context: Context, val workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        Log.i("aaa", "UploadLocation -> 要上传的地址 = " + inputData.getString("location") ?: "none")
        return Result.success()
    }
}