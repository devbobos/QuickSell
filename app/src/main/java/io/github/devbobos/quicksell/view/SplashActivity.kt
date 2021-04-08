package io.github.devbobos.quicksell.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.devbobos.quicksell.R
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {
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
        val intent = Intent(baseContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}