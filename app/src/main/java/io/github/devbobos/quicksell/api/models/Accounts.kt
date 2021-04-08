package io.github.devbobos.quicksell.api.models

data class Accounts(
    val currency : String,
    val balance : String,
    val locked : String,
    val avg_buy_price : String,
    val avg_buy_price_modified : Boolean,
    val unit_currency : String,
)
