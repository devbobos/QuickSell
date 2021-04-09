package io.github.devbobos.quicksell.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import io.github.devbobos.quicksell.ApplicationCache
import io.github.devbobos.quicksell.BuildConfig
import io.github.devbobos.quicksell.R
import io.github.devbobos.quicksell.helper.utils.Utils
import kotlinx.coroutines.*


class SplashActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)
        GlobalScope.launch(Dispatchers.Default) {
            delay(500L)
            withContext(Dispatchers.Main) {
                start()
            }
        }
    }

    private fun initLogger(){
        Logger.addLogAdapter(object : AndroidLogAdapter() {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
//                return BuildConfig.DEBUG
                return true
            }
        })
    }

    private fun start() {
        val intent:Intent
        if(shouldMoveAuthActivity()){
            intent = Intent(baseContext, AuthActivity::class.java)
        } else{
            intent = Intent(baseContext, HomeActivity::class.java)
        }
        startActivity(intent)
        finish()
    }

    private fun shouldMoveAuthActivity(): Boolean{
        if(Utils.isEmpty(ApplicationCache.getInstance().jwtToken)){
            return true
        }
        if(Utils.isEmpty(ApplicationCache.getInstance().accessKey)){
            return true
        }
        if(Utils.isEmpty(ApplicationCache.getInstance().secretKey)){
            return true
        }
        return false
    }
}