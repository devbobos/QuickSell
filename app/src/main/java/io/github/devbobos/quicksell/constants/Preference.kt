package io.github.devbobos.quicksell.constants

enum class Preference {
    USER_ACCESS_KEY,
    USER_SECRET_KEY,
    USER_SELECTED_MARKET_ID,
    PRICE_TYPE_BUY,
    PRICE_TYPE_SELL,
}

enum class PriceType{
    MANUAL_PRICE, //지정가
    MARKET_PRICE //시장가
}