package io.github.devbobos.quicksell.api.models

data class MarketInfo(
    val market: String, //업비트에서 제공중인 시장 정보
    val korean_name: String,
    val english_name: String,
    val market_warning: String,
)
