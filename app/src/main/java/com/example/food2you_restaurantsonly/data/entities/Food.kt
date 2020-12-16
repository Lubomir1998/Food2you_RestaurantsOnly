package com.example.food2you_restaurantsonly.data.entities

import com.google.gson.annotations.Expose
import java.util.*

data class Food(
    val name: String,
    val type: String,
    val weight: Int,
    val imgUrl: String,
    val price: Float,
    @Expose(serialize = false, deserialize = false)
    var isInBasket: Boolean = false,
    val restaurantName: String,
    val id: String = UUID.randomUUID().toString()
)
