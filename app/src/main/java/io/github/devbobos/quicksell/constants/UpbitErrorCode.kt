package io.github.devbobos.quicksell.constants

enum class UpbitErrorCode(val message: String) {
    //400 Bad Request
    create_ask_error("주문 요청 정보가 올바르지 않습니다."),
    create_bid_error("주문 요청 정보가 올바르지 않습니다."),
    insufficient_funds_ask("매수/매도 가능 잔고가 부족합니다."),
    insufficient_funds_bid("매수/매도 가능 잔고가 부족합니다."),
    under_min_total_ask("주문 요청 금액이 최소주문금액 미만입니다."),
    under_min_total_bid("주문 요청 금액이 최소주문금액 미만입니다."),
    withdraw_address_not_registerd("허용되지 않은 출금 주소입니다."),
    validation_error("잘못된 API 요청입니다."),

    //401 Unauthorized
    invalid_query_payload("JWT 헤더의 페이로드가 올바르지 않습니다."),
    jwt_verification("JWT 헤더 검증에 실패했습니다."),
    expired_access_key("API 키가 만료되었습니다."),
    nonce_used("이미 요청한 nonce값이 다시 사용되었습니다."),
    no_authorization_i_p("허용되지 않은 IP 주소입니다."),
    out_of_scope("허용되지 않은 기능입니다."),
}