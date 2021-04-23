package io.github.devbobos.quicksell.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import io.github.devbobos.quicksell.BaseActivity
import io.github.devbobos.quicksell.R
import io.github.devbobos.quicksell.view.home.HomeActivity
import kotlinx.android.synthetic.main.warning_activity.*

class WarningActivity: BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.warning_activity)
        warning_button_submit.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.warning_button_submit -> {
                start()
            }
        }
    }

    private fun start() {
        val intent: Intent
        if(shouldMoveAuthActivity()){
            intent = Intent(baseContext, AuthActivity::class.java)
        } else{
            intent = Intent(baseContext, HomeActivity::class.java)
        }
        startActivity(intent)
        finish()
    }

    private fun shouldMoveAuthActivity(): Boolean{
        return false
//        if(Utils.isEmpty(ApplicationCache.getInstance().jwtToken)){
//            return true
//        }
//        if(Utils.isEmpty(ApplicationCache.getInstance().accessKey)){
//            return true
//        }
//        if(Utils.isEmpty(ApplicationCache.getInstance().secretKey)){
//            return true
//        }
//        return false
    }
}