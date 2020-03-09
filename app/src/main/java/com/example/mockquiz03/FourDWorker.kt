package com.example.mockquiz03

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay


class FourDWorker(private val c: Context, param: WorkerParameters) : Worker(c,param){
    var currentNum = 0L
    override fun doWork(): Result {
        var number = inputData.getLong("number",-1)
        var temp = highlySecretiveYetEffectiveCostGenerator(number.toInt())
        return Result.success(createInputData(temp))

    }


    fun createInputData(num:Float): Data {
        return Data.Builder()
            .putFloat("number", num)
            .build()
    }



    fun highlySecretiveYetEffectiveCostGenerator(fourDigitNum: Int): Float {
        var cost = 0f
        val numStr = Integer.toString(fourDigitNum)
        val digits = IntArray(numStr.length)
        for (i in 0 until numStr.length) {
            digits[i] = Character.getNumericValue(numStr[i])
        }
        for (i in 0 until utils.securityIters) {
            cost += digits[(Math.random() * 3).toInt()] * digits[(Math.random() * 3).toInt()].toFloat()
            for (j in 3 downTo 0) {
                if (digits[j] > 0) cost /= digits[j]
            }
        }
        return Math.round(cost * 8800f) / 100f // a little bit more luck here
    }

}