package com.example.food2you_restaurantsonly.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import java.util.*

@Entity
data class Food(
    val name: String,
    val type: String,
    val weight: Int,
    val imgUrl: String,
    val price: Float,
    @Expose(serialize = false, deserialize = false)
    var isInBasket: Boolean = false,
    val restaurantName: String,
    @PrimaryKey(autoGenerate = false)
    val id: String = UUID.randomUUID().toString()
)
