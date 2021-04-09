package io.github.devbobos.quicksell.view

import android.os.Bundle
import io.github.devbobos.quicksell.BaseActivity
import io.github.devbobos.quicksell.R

class HomeActivity: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
    }
}