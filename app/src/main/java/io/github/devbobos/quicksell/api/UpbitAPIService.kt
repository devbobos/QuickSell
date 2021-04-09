package io.github.devbobos.quicksell.api

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.github.devbobos.quicksell.ApplicationCache
import io.github.devbobos.quicksell.Base
import io.github.devbobos.quicksell.api.models.ApiKey
import io.github.devbobos.quicksell.api.models.ErrorResponse
import io.github.devbobos.quicksell.helper.utils.Utils
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class UpbitAPIService {
    val defaultContentType = "application/json"
    val expireDateDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"

    fun refreshJwtToken(accessKey: String, secretKey: String): Response<List<ApiKey>>?{
        val algorithm: Algorithm = Algorithm.HMAC256(secretKey)
        val token: String = JWT.create()
            .withClaim("access_key", accessKey)
            .withClaim("nonce", UUID.randomUUID().toString())
            .sign(algorithm)

        val jwtToken = "Bearer $token"
        ApplicationCache.getInstance().jwtToken = jwtToken
        ApplicationCache.getInstance().accessKey = accessKey
        ApplicationCache.getInstance().secretKey = secretKey
        val request = getRequest()
        request?.let {
            val call:Call<List<ApiKey>> = it.requestApiKeys(defaultContentType, jwtToken);
            val response: Response<List<ApiKey>> = call.execute()
            val apiKeyList = response.body()
            if(Utils.hasItems(apiKeyList)){
                apiKeyList?.let {
                    for(item in apiKeyList){
                        if(accessKey.equals(item.access_key)){
                            ApplicationCache.getInstance().accessKeyExpireDate = item.expire_at
                        }
                    }
                }
            }
            return response
        }
        return null
    }

    fun checkAccessKeyExpired(): Boolean{
        val accessKeyExpireDate = ApplicationCache.getInstance().getAccessKeyExpireDate()
        if(Utils.isEmpty(accessKeyExpireDate)){
            return true
        }
        val nowDate = Date()
        val simpleDateFormat = SimpleDateFormat(expireDateDateFormat)
        try {
            val expireDate = simpleDateFormat.parse(accessKeyExpireDate)
            if(expireDate.before(nowDate)){
                return true
            }
            return false;
        }catch (e: ParseException){
            return true;
        }
    }

    fun cancelAllRequest(){
        getOkHttpClient().dispatcher.cancelAll()
    }

    private fun getOkHttpClient(): OkHttpClient{
        if(Utils.isNull(Base.okHttpClient)){
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
            Base.okHttpClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
        }
        return Base.okHttpClient
    }

    private fun getRetrofit(): Retrofit{
        val baseUrl = "https://api.upbit.com/"
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit
    }

    private fun getRequest(): UpbitAPI? {
        return getRetrofit().create(UpbitAPI::class.java)
    }

    fun parseErrorResponse(response: ResponseBody?): ErrorResponse?{
        val converter: Converter<ResponseBody, ErrorResponse> = getRetrofit()
            .responseBodyConverter(ErrorResponse::class.java, arrayOfNulls<Annotation>(0))

        val error: ErrorResponse?

        try {
            error = converter.convert(response)
        } catch (e: IOException) {
            return null
        }
        error?.let {
            if(Utils.isEmpty(it.name)){
                return null
            }
        }

        return error
    }
}
