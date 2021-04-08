package io.github.devbobos.quicksell.api.models

data class Order(
    val uuid : String,
    val side : String,
    val ord_type : String,
    val price : String,
    val avg_price : String,
    val state : String,
    val market : String,
    val created_at : String,
    val volume : String,
    val remaining_volume : String,
    val reserved_fee : String,
    val remaining_fee : String,
    val paid_fee : String,
    val locked : String,
    val executed_volume : String,
    val trade_count : Integer,
)
