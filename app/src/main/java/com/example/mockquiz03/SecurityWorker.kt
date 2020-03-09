package com.example.mockquiz03

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class SecurityWorker(c: Context, param: WorkerParameters): Worker(c,param){
    val c =c
    lateinit var notiMgr : NotificationManager
    override fun doWork(): Result {
        notiMgr = NotificationServices.getNotiMgr(c)
        utils.securityIters+=8
        Log.d("MainActivity","${utils.securityIters}")
        val (noti,id) = NotificationServices.buildNotification(
            c,
            MainActivity::class.java,
            utils.NOTIFICATION_CHANNEL_ID,
            "POSit Security",
            "Your cost calculator is now 8 times more secure!"
        )
        notiMgr.notify(1,noti)
        return Result.success()
    }
}