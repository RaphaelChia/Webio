package com.example.mockquiz03

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

class NotificationServices(){


    companion object{
        val min=1
        val max=5
        var listOfId:ArrayList<Int> = arrayListOf()


        /**
         *  This must be called first always, in order to get a notification manager to carry
         *  out any other functions.
         */
        fun getNotiMgr(ctx:Context):NotificationManager{
            return ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }

        /**************************************
         *  Creates a notification channel to direct notifications to
         *  @param nmgr             notification manager to create the channel
         *  @param id               notification channel id. Must be same if you want it grouped
         *  @param name             notification channel name. idk why
         *  @param importanceLevel  notification level importance level.
         *                          4 - Shows everywhere, makes noise and peeks.
         *                          3 - Shows everywhere, makes noise, but does not visually intrude.
         *                          2 - Shows in the shade, not audibly intrusive
         *                          1 - Only shows in the shade, below the fold
         *  @sample createNotificationChannel(notiMgr,"GameNoti","Games Notifications",4)
         */
        fun createNotificationChannel(nmgr:NotificationManager,id:String,name:String,importanceLevel:Int){
            nmgr.createNotificationChannel(
                NotificationChannel(
                    id,
                    name,
                    importanceLevel
                )
            )
        }


        /**
         *  This function sets up the notification object to be ready to be called at
         *  NotificationManager.Notify(id,notification)
         *  @param ctx              The context from the activity creating this notification
         *  @param intendedIntent   The intended intent to launch open when user click on notif
         *  @param notiChannelId    The channel id that the notification is going into
         *  @param notiTitle        The notification title when peek
         *  @param notiMsg          the notification content when peek
         *  @return                 Pair<Notification,Int> so you need to deconstruct when receiving
         *                          it. The Integer is the autoGenerated pendingIntent ID. You need
         *                          to use this ID to retrieve the pendingIntent again(cancel etc.)
         *  @sample buildNotification(this,MainActivity::class.java,"GameNoti","Chest Ready!",
         *                              "please log in to open your chest!")
         */
        fun <T>buildNotification(ctx:Context, intendedIntent:Class<T>, notiChannelId:String,
                                 notiTitle:String, notiMsg:String): Pair<Notification,Int> {
            var pendingIntentId = -1
            if(listOfId.size== max){
                listOfId = arrayListOf()
            }
            while(pendingIntentId<0){
                pendingIntentId=
                    generateRandId()
            }
            listOfId.add(pendingIntentId)


            val launchIntent = Intent(ctx,intendedIntent)
            val pendingIntent = PendingIntent.getActivity(
                ctx,
                pendingIntentId,
                launchIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)

            val noti = Notification.Builder(ctx,notiChannelId)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle(notiTitle)
                .setContentText(notiMsg)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .build()

            return Pair(noti,pendingIntentId)
        }

        fun generateRandId():Int{
            val ret = (min..max).shuffled().first()
            if(ret in listOfId){
                return -1
            }
            return ret
        }
    }







}