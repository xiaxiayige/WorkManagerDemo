package com.xiaxiayige.workmanagerdemo

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private lateinit var workManager: WorkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        workManager = WorkManager.getInstance(this)
        //getLocation
        findViewById<Button>(R.id.btn_getlocation)
            .setOnClickListener {
//                getLocation()
//                getAnduploadLocation()
//                testPeriodic()
                testConstraints()
            }
    }

    /**
     * 测试约束条件-网络连接的时候执行
     *
     * 测试 1 步骤
     * 1.   断网，执行方法
     * 2.   杀掉程序
     * 3.   联网
     * getLocation会运行吗？
     *
     * 测试 2 步骤
     *  1.   断网，执行方法
     *  2.   杀掉程序
     *  3.   联网
     *  4.  进入程序 ，
     * getLocation会运行吗？
     *
     * 测试 3 步骤
     *  1.   断网，执行方法
     *  2.   重启手机
     *  3.   联网
     *  4.  进入程序 ，
     *  getLocation会运行吗？
     */
    private fun testConstraints() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val getLocationWorker = OneTimeWorkRequestBuilder<LocationWorker>()
            .setConstraints(constraints)
            .build()

        workManager.beginWith(getLocationWorker).enqueue()

        Log.i("aaa", "testConstraints")
    }

    /**
     * 周期内运行的任务
     */
    private fun testPeriodic() {
        val getLocation =
            PeriodicWorkRequestBuilder<LocationWorker>(1, TimeUnit.MINUTES).build()
        workManager.enqueue(getLocation)
    }

    /**
     * 获取+上传
     */
    private fun getAnduploadLocation() {
        val getLocationWorkerRequest = OneTimeWorkRequestBuilder<LocationWorker>().build()
        val uploadRequest = OneTimeWorkRequestBuilder<UploadLocation>().build()

        val workQuery =
            WorkQuery.Builder.fromIds(listOf(getLocationWorkerRequest.id, uploadRequest.id)).build()

        workManager.getWorkInfosLiveData(workQuery).observe(this, { listWorkInfo ->
            if (listWorkInfo.size > 0) {
                if (listWorkInfo[0] != null && listWorkInfo[0].state == WorkInfo.State.SUCCEEDED) {
                    Log.i("aaa", "获取地址成功，等待上传")
                } else {
                    Log.i("aaa", "获取地址状态 = " + listWorkInfo[0].state)
                }

                if (listWorkInfo[1] != null && listWorkInfo[1].state == WorkInfo.State.SUCCEEDED) {
                    Log.i("aaa", "上传地址成功")
                } else {
                    Log.i("aaa", "上传地址状态 = " + listWorkInfo[1].state)
                }
            }

        })
        //beginUniqueWork 避免重复添加 Work

        workManager.beginUniqueWork(
            "testxxxxx",
            ExistingWorkPolicy.KEEP, //@See More
            getLocationWorkerRequest
        ).
        then(uploadRequest).enqueue()

    }

    /**
     * 上传
     */
    private fun uploadLocation(data: Data) {
        val uploadRequest = OneTimeWorkRequestBuilder<UploadLocation>()
            .setInputData(data).build()
        workManager.beginWith(uploadRequest).enqueue()
        workManager.getWorkInfoByIdLiveData(uploadRequest.id)
            .observe(this, { info ->
                if (info != null && info.state.isFinished) {
                    Log.i("aaa", "uploadLocation --> ${info}")
                }
            })
    }

    /**
     * 获取地址
     */
    private fun getLocation() {
        val getLocationWorkerRequest = OneTimeWorkRequestBuilder<LocationWorker>().build()
        workManager.enqueue(getLocationWorkerRequest)
        workManager.getWorkInfoByIdLiveData(getLocationWorkerRequest.id)
            .observe(this, { info ->
                if (info != null && info.state.isFinished) {
                    val outputData = info.outputData
                    Log.i("aaa", "getLocation --> ${outputData}")
//                    uploadLocation(outputData)
                }
            })
    }

    fun longruntime(view: View) {
        val downloadWork = OneTimeWorkRequestBuilder<DownloadWork>().build()
        val locationWorker = OneTimeWorkRequestBuilder<LocationWorker>().build()
        workManager
            .beginUniqueWork("download", ExistingWorkPolicy.KEEP, locationWorker)
            .enqueue()
    }
}