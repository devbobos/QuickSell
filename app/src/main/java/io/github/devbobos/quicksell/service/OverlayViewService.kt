package io.github.devbobos.quicksell.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import io.github.devbobos.quicksell.Base
import io.github.devbobos.quicksell.Base.context
import io.github.devbobos.quicksell.R
import io.github.devbobos.quicksell.api.UpbitAPIService

class OverlayViewService : Service(){
    val notificationId = 1;
    val notificationChannelId = Base.context.packageName+".notificationChannelId"
    val notificationChannelName = ""
    val requestCodeStop = 2;
    val intentActionStop = "STOP"
    val upbitAPIService = UpbitAPIService()
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        startForeground(notificationId, getNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val intentAction = it.action
            when(intentAction){
                intentActionStop -> {
                    stopForeground(true)
                    stopSelf()
                }
            }
        }
        return Service.START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        upbitAPIService.cancelAllRequest()
    }

    fun getNotification(): Notification{
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT >= 26){
            val notificationChannel = notificationManager.getNotificationChannel(
                notificationChannelId
            )
            if (notificationChannel == null) {
                val channel = NotificationChannel(
                    notificationChannelId,
                    notificationChannelName,
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationManager.createNotificationChannel(channel)
            }
        }
        val builder: NotificationCompat.Builder;
        if(Build.VERSION.SDK_INT >= 26){
            builder = NotificationCompat.Builder(Base.context, notificationChannelId)
        }else{
            builder = NotificationCompat.Builder(Base.context)
        }
        val buttonClickIntent = Intent(context, OverlayViewService::class.java)
        buttonClickIntent.setAction(intentActionStop)
        val pendingIntent = PendingIntent.getService(context, requestCodeStop, buttonClickIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val buttonClickNotificationAction = NotificationCompat.Action(R.drawable.ic_baseline_close_24, "종료하기", pendingIntent)
        builder.setAutoCancel(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(Notification.PRIORITY_MAX)
            .setShowWhen(false)
            .setContentTitle("실행중입니다")
            .addAction(buttonClickNotificationAction)
        return builder.build()
    }
}