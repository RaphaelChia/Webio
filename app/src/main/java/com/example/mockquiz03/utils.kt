package com.example.mockquiz03

import kotlin.random.Random

class utils{
   companion object {
       val NOTIFICATION_CHANNEL_ID = "MockTest"
       val NOTIFICATION_CHANNEL_NAME = "POSit Notification"
       val NOTIFICATION_ID = 1000
       val GENERATOR_DELAY = 888L

       var securityIters = 8888888

       fun generateRand():Long{
           return Random.nextLong(1000,9999)
       }
   }
}