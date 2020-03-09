package com.example.mockquiz03

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Binder
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.mockquiz03.NotificationServices as ns

class POSitService : Service(), CoroutineScope by MainScope() {

    private val binder = LocalBinder()
    private lateinit var sharedPref : SharedPreferences
    private var currentNum : Long = 0L

    lateinit var notiMgr :NotificationManager

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        sharedPref = getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        notiMgr = ns.getNotiMgr(this)
        ns.createNotificationChannel(notiMgr,
            utils.NOTIFICATION_CHANNEL_ID,
            utils.NOTIFICATION_CHANNEL_NAME,4)
        launch {
            while(true){
                generate4dNum()
            }

        }
        return START_STICKY
    }

    inner class LocalBinder: Binder(){
        fun getService(): POSitService {
            return this@POSitService

        }
    }

    suspend fun generate4dNum(){
        delay(utils.GENERATOR_DELAY)
        this.currentNum = utils.generateRand()
        val (noti,id) = ns.buildNotification(
            this,
            MainActivity::class.java,
            utils.NOTIFICATION_CHANNEL_ID,
            "${utils.NOTIFICATION_CHANNEL_NAME}",
            "Current 4D: ${this.currentNum}"
        )
        notiMgr.notify(utils.NOTIFICATION_ID,noti)
    }

    fun getNumber():Long{
        return this.currentNum
    }


}