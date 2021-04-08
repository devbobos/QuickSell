package io.github.devbobos.quicksell.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import io.github.devbobos.quicksell.R
import kotlinx.coroutines.*

class SplashActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)
        GlobalScope.launch(Dispatchers.Default) {
            delay(500L)
            withContext(Dispatchers.Main) {
                goMain()
            }
        }
    }

    private suspend fun goMain() {
        val intent = Intent(baseContext, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }
}