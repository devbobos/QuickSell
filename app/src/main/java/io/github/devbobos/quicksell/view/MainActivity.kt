package io.github.devbobos.quicksell.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.github.devbobos.quicksell.BaseActivity
import io.github.devbobos.quicksell.R
import io.github.devbobos.quicksell.Utils
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        main_button_redirectUpbit.setOnClickListener(this)
        main_button_submit.setOnClickListener(this)
        main_button_getKeyGuide.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.main_button_redirectUpbit -> {
                redirectUpbit()
            }
            R.id.main_button_submit -> {
                submit()
            }
            R.id.main_button_getKeyGuide -> {
                getKeyGuide()
            }
        }
    }

    private fun redirectUpbit(){
        val url = "https://upbit.com/mypage/open_api_management"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun getKeyGuide(){
        val url = "https://upbit.com/service_center/open_api_guide"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun submit(){
        if(Utils.isEmpty(main_textInputEditText_accessKey.text)){
            val message = "Access Key를 입력해주세요"
            showSnackBar(message)
            main_textView_resonseMessage.setText(message)
            return
        }
        if(Utils.isEmpty(main_textInputEditText_secretKey.text)){
            val message = "Secret Key를 입력해주세요"
            showSnackBar(message)
            main_textView_resonseMessage.setText(message)
            return
        }
        main_textView_resonseMessage.setText("")
        main_progressBar.visibility = View.VISIBLE
        GlobalScope.launch(Dispatchers.IO) {
            val accessKey = main_textInputEditText_accessKey.text.toString()
            val secretKey = main_textInputEditText_secretKey.text.toString()
            val algorithm: Algorithm = Algorithm.HMAC256(secretKey)

            val jwtToken: String = JWT.create()
                .withClaim("access_key", accessKey)
                .withClaim("nonce", UUID.randomUUID().toString())
                .sign(algorithm)
            val authenticationToken = "Bearer $jwtToken"
            withContext(Dispatchers.Main){
                main_progressBar.visibility = View.GONE
            }
        }
    }
}