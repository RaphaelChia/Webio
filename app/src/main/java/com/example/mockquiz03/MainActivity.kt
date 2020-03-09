package com.example.mockquiz03

import android.app.ActivityManager
import android.app.NotificationManager
import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.*
import com.example.mock_quiz_03.R


class MainActivity : AppCompatActivity() {
    private lateinit var sharedPref : SharedPreferences
    private lateinit var mService : POSitService
    private var mBound = false
    var securityIters = 888888
    lateinit var notiMgr:NotificationManager
    val connection = object: ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            mBound = false
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as POSitService.LocalBinder
            mService = binder.getService()
            mBound = true

        }

    }

    override fun onStart(){
        super.onStart()
        Log.d("MainActivity","On Start activity")
        Intent(this, POSitService::class.java).also{
                intent -> bindService(intent,connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop(){
        super.onStop()
        Log.d("MainActivity","On Stop activity")

        unbindService(connection)
        mBound = false
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val generateCostBtn = findViewById<Button>(R.id.generateCostBtn)
        val textField = findViewById<TextView>(R.id.costTextView)

        val workMgr = WorkManager.getInstance(application)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        notiMgr = NotificationServices.getNotiMgr(this)
        if(!isMyServiceRunning(POSitService::class.java)){
            startService(Intent(this,POSitService::class.java))
        }



        generateCostBtn.setOnClickListener{
            add8TimesSecurityToHighlySecretiveYetEffectiveCostGenerator()
            val generateCost = OneTimeWorkRequestBuilder<FourDWorker>()
                .setConstraints(constraints)
                .setInputData(createInputData(mService.getNumber()))
                .build()
            workMgr.enqueue(generateCost)

            workMgr.getWorkInfoByIdLiveData(generateCost.id).observe(this, Observer { wi->
                if(wi!=null && wi.state.isFinished){
                    Log.d("main","work finished.")

                    textField.text = "Cost: $${ wi.outputData.getFloat("number",-1F)}"
                }
            }

            )
            val (noti,id) = NotificationServices.buildNotification(
                this,
                MainActivity::class.java,
                utils.NOTIFICATION_CHANNEL_ID,
                "POSit Security",
                "Your cost calculator is now 8 times more secure!"
            )
            notiMgr.notify(1,noti)
        }

    }

    fun createInputData(num:Long): Data {
        return Data.Builder()
            .putLong("number", num)
            .build()
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    fun add8TimesSecurityToHighlySecretiveYetEffectiveCostGenerator(){
        utils.securityIters+=8
    }


}
