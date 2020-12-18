package com.example.food2you_restaurantsonly.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
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
    val owner: String,
    @PrimaryKey(autoGenerate = false)
    val id: String = UUID.randomUUID().toString()
)
