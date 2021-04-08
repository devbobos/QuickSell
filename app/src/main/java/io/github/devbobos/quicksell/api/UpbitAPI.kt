package io.github.devbobos.quicksell.api

import io.github.devbobos.quicksell.api.responses.Accounts
import io.github.devbobos.quicksell.api.responses.MarketInfos
import io.github.devbobos.quicksell.api.responses.Order
import retrofit2.Call
import retrofit2.http.*

interface UpbitAPI {

//    전체 계좌 조회
//    내가 보유한 자산 리스트를 보여줍니다.
    @GET("v1/accounts")
    fun getAccounts(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") authorization: String,
    ): Call<List<Accounts>>

//    마켓 코드 조회
//    업비트에서 거래 가능한 마켓 목록
    @GET("v1/market/all")
    fun getMarketInfos(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") authorization: String,
        @Query("isDetails") isDetails: Boolean,
    ): Call<List<MarketInfos>>

//    주문하기
//    주문 요청을 한다.
    @POST("v1/orders")
    fun requestOrder(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") authorization: String,
        @Field("market") market: String,    //마켓 ID (필수)
        @Field("side") side: String, //주문 종류 (필수) bid : 매수, ask : 매도
        @Field("volume") volume: String, //주문량 (지정가, 시장가 매도 시 필수)
        @Field("price") price: String,  //주문 가격. (지정가, 시장가 매수 시 필수)
        @Field("ord_type") ord_type: String,    //주문 타입 (필수) limit : 지정가 주문, price : 시장가 주문(매수), market : 시장가 주문(매도)
    ): Call<List<Order>>

    //매수
    @POST("v1/orders")
    fun requestOrderBuy(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") authorization: String,
        @Field("market") market: String,    //마켓 ID (필수)
        @Field("side") side: String, //주문 종류 (필수) bid : 매수, ask : 매도
        @Field("price") price: String,  //주문 가격. (지정가, 시장가 매수 시 필수)
        @Field("ord_type") ord_type: String,    //주문 타입 (필수) limit : 지정가 주문, price : 시장가 주문(매수), market : 시장가 주문(매도)
    ): Call<List<Order>>

    //매도
    @POST("v1/orders")
    fun requestOrderSell(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") authorization: String,
        @Field("market") market: String,    //마켓 ID (필수)
        @Field("side") side: String, //주문 종류 (필수) bid : 매수, ask : 매도
        @Field("volume") volume: String, //주문량 (지정가, 시장가 매도 시 필수)
        @Field("ord_type") ord_type: String,    //주문 타입 (필수) limit : 지정가 주문, price : 시장가 주문(매수), market : 시장가 주문(매도)
    ): Call<List<Order>>
}