package com.example.food2you_restaurantsonly.data.remote.requests

data class UpdateOrderStatusRequest(
    val orderId: String,
    val newStatus: String
)
