package io.github.devbobos.quicksell.api.models

data class Accounts(
    val currency : String,    //화폐를 의미하는 영문 대문자 코드
    val balance : String,    //주문가능 금액/수량
    val locked : String,    //주문 중 묶여있는 금액/수량
    val avg_buy_price : String,    //매수평균가
    val avg_buy_price_modified : Boolean,    //매수평균가 수정 여부
    val unit_currency : String,    //평단가 기준 화폐
)
