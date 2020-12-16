package com.example.food2you_restaurantsonly.data.entities

import java.util.*

data class Restaurant(
    val name: String,
    val type: String,
    val kitchen: String,
    val deliveryPrice: Float,
    val deliveryTimeMinutes: Int,
    val minimalPrice: Int,
    val imgUrl: String,
    val previews: List<String>,
    val users: List<String>,
    val owners: List<String>,
    val id: String = UUID.randomUUID().toString()
)
