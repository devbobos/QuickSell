package io.github.devbobos.quicksell

import android.app.Application

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Base.context = applicationContext
    }
}