package io.github.devbobos.quicksell.api.models

data class MarketOrderInfo(
    val bid_fee : String,    //매수 수수료 비율
    val ask_fee : String,    //매도 수수료 비율
    val market : market,    //마켓에 대한 정보
    val bid_account: bid_account,//매수 시 사용하는 화폐의 계좌 상태
    val ask_account: ask_account,//매도 시 사용하는 화폐의 계좌 상태
)
data class market(  //마켓에 대한 정보
    val id : String,    //마켓의 유일 키
    val name : String,    //마켓 이름
    val order_types : List<String>,    //지원 주문 방식
    val order_sides : List<String>,    //지원 주문 종류
    val bid : bid,    //매수 시 제약사항
    val ask : ask,    //매도 시 제약사항
    val max_total : String,    //최대 매도/매수 금액
    val state : String,    //마켓 운영 상태
)
data class bid(//매수 시 제약사항
    val currency : String,    //화폐를 의미하는 영문 대문자 코드
    val price_unit : String,    //주문금액 단위
    val min_total : Integer,    //최소 매도/매수 금액
)
data class ask(//매도 시 제약사항
    val currency : String,    //화폐를 의미하는 영문 대문자 코드
    val price_unit : String,    //주문금액 단위
    val min_total : Integer,    //최소 매도/매수 금액
)
data class bid_account( //매수 시 사용하는 화폐의 계좌 상태
    val currency : String,    //화폐를 의미하는 영문 대문자 코드
    val balance : String,    //주문가능 금액/수량
    val locked : String,    //주문 중 묶여있는 금액/수량
    val avg_buy_price : String,    //매수평균가
    val avg_buy_price_modified : Boolean,    //매수평균가 수정 여부
    val unit_currency : String,    //평단가 기준 화폐
)
data class ask_account( //매도 시 사용하는 화폐의 계좌 상태
    val currency : String,    //화폐를 의미하는 영문 대문자 코드
    val balance : String,    //주문가능 금액/수량
    val locked : String,    //주문 중 묶여있는 금액/수량
    val avg_buy_price : String,    //매수평균가
    val avg_buy_price_modified : Boolean,    //매수평균가 수정 여부
    val unit_currency : String,    //평단가 기준 화폐
)