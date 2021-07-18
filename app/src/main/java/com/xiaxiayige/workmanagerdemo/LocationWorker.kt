package com.xiaxiayige.workmanagerdemo

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

class LocationWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        val data = Data.Builder()
            .putString("location", getLocation())
            .build()
        return Result.failure()
    }

    private fun getLocation(): String {
        Log.i("aaa","getLocation")
        return "杭州市滨江区xxx"
    }
}