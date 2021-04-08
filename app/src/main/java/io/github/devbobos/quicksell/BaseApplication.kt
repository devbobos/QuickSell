package io.github.devbobos.quicksell

import android.app.Application
import com.thefinestartist.Base

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Base.initialize(applicationContext)
    }
}