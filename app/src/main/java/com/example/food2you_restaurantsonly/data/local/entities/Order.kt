package com.example.food2you_restaurantsonly.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "order")
data class Order(
    var restaurant: String = "",
    var address: String = "",
    var recipient: String = "",
    var email: String = "",
    var phoneNumber: String = "",
    var food: List<FoodItem> = listOf(),
    var price: Float = 0f,
    var timestamp: Long = 0L,
    var status: String = "",
    val resImgUrl: String,
    val restaurantName: String,
    var resId: String = "",
    @PrimaryKey(autoGenerate = false)
    val id: String = UUID.randomUUID().toString()
)
