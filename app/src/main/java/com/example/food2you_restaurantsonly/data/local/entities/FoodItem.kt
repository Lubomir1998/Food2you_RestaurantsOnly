package com.example.food2you_restaurantsonly.data.local.entities

data class FoodItem(
    val name: String,
    val price: Float,
    var quantity: Int = 1
)
