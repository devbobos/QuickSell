package io.github.devbobos.quicksell.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

class OverlayViewService : Service(){
    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return Service.START_NOT_STICKY
    }
}