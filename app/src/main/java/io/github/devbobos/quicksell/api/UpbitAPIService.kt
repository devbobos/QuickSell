package io.github.devbobos.quicksell.api

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.github.devbobos.quicksell.BaseCache
import io.github.devbobos.quicksell.api.models.ApiKey
import io.github.devbobos.quicksell.helper.Utils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class UpbitAPIService {
    val DEFAULT_CONTENT_TYPE = "application/json"
    val EXPIRE_DATE_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"

    fun refreshJwtToken(){
        val accessKey = BaseCache.getInstance().accessKey
        val secretKey = BaseCache.getInstance().secretKey
        val algorithm: Algorithm = Algorithm.HMAC256(secretKey)

        val token: String = JWT.create()
            .withClaim("access_key", accessKey)
            .withClaim("nonce", UUID.randomUUID().toString())
            .sign(algorithm)

        val jwtToken = "Bearer $token"
        BaseCache.getInstance().jwtToken = jwtToken
        val request = getRequest()
        request?.let {
            val call = it.requestApiKeys(DEFAULT_CONTENT_TYPE, jwtToken);
            call.enqueue(object: Callback<List<ApiKey>>{
                override fun onResponse(
                    call: Call<List<ApiKey>>,
                    response: Response<List<ApiKey>>
                ) {

                    TODO("Not yet implemented")
                }

                override fun onFailure(call: Call<List<ApiKey>>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

    fun checkAccessKeyExpired(): Boolean{
        val accessKeyExpireDate = BaseCache.getInstance().getAccessKeyExpireDate()
        if(Utils.isEmpty(accessKeyExpireDate)){
            return true
        }
        val nowDate = Date()
        val simpleDateFormat = SimpleDateFormat(EXPIRE_DATE_DATE_FORMAT)
        try {
            val expireDate = simpleDateFormat.parse(accessKeyExpireDate)
            if(expireDate.before(nowDate)){
                return true
            }
            return false;
        }catch (e : ParseException){
            return true;
        }
    }

    private fun getRequest(): UpbitAPI? {
        val baseUrl = "https://api.upbit.com/"
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .build()
        return retrofit.create(UpbitAPI::class.java)
    }
}
