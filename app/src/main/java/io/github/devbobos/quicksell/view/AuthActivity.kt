package io.github.devbobos.quicksell.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import io.github.devbobos.quicksell.*
import io.github.devbobos.quicksell.api.UpbitAPIService
import io.github.devbobos.quicksell.constants.PreferenceKey
import io.github.devbobos.quicksell.helper.security.AES256
import io.github.devbobos.quicksell.helper.utils.Utils
import io.github.devbobos.quicksell.view.home.HomeActivity
import kotlinx.android.synthetic.main.auth_activity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_activity)
        auth_button_redirectUpbit.setOnClickListener(this)
        auth_button_submit.setOnClickListener(this)
        auth_button_getKeyGuide.setOnClickListener(this)
        setVersion()
        auth_textView_resonseMessage.setText("")
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.auth_button_redirectUpbit -> {
                redirectUpbit()
            }
            R.id.auth_button_submit -> {
                submit()
            }
            R.id.auth_button_getKeyGuide -> {
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
        if(Utils.isEmpty(auth_textInputEditText_accessKey.text)){
            val message = "Access Key를 입력해주세요"
            auth_textView_resonseMessage.setText(message)
            return
        }
        if(Utils.isEmpty(auth_textInputEditText_secretKey.text)){
            val message = "Secret Key를 입력해주세요"
            auth_textView_resonseMessage.setText(message)
            return
        }
        auth_textView_resonseMessage.setText("")
        auth_progressBar.visibility = View.VISIBLE
        GlobalScope.launch(Dispatchers.IO) {
            val accessKey = auth_textInputEditText_accessKey.text.toString()
            val secretKey = auth_textInputEditText_secretKey.text.toString()
            val service = UpbitAPIService()
            val response = service.refreshJwtToken(accessKey, secretKey)
            if(Utils.notNull(response)){
                saveCacheDataToPreference()
            }
            withContext(Dispatchers.Main){
                auth_progressBar.visibility = View.GONE
                if(Utils.isNull(response)){
                    val message = "연결 실패"
                    auth_textView_resonseMessage.setText(message)
                } else{
                    response?.let {
                        when(it.code()){
                            200 -> {
                                val Intent = Intent(applicationContext, HomeActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            else -> {
                                val errorResponse = service.parseErrorResponse(it.errorBody())
                                if(Utils.isNull(errorResponse)){
                                    setErrorMessage("${it.code()}", it.message())
                                } else{
                                    setErrorMessage(errorResponse!!.name, errorResponse!!.message);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun saveCacheDataToPreference(){
        val encrypter = AES256()
        Utils.put(PreferenceKey.STRING_USER_ACCESS_KEY.name, encrypter.encrypt(ApplicationCache.getInstance().accessKey))
        Utils.put(PreferenceKey.STRING_USER_ACCESS_KEY_EXPIRE_DATE.name, ApplicationCache.getInstance().accessKeyExpireDate)
        Utils.put(PreferenceKey.STRING_USER_SECRET_KEY.name, encrypter.encrypt(ApplicationCache.getInstance().secretKey))
    }

    private fun setErrorMessage(code:String, message:String){
        var value = "${message}"
        if(Utils.isEmpty(message)){
            value = "키를 다시 확인해주세요"
        }
        auth_textView_resonseMessage.setText(value)
    }

    private fun setVersion(){
        val version = "${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE})"
        auth_textView_version.setText(version)
    }
}