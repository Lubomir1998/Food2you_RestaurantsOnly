package com.example.food2you_restaurantsonly.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.food2you_restaurantsonly.data.local.entities.Food
import com.example.food2you_restaurantsonly.data.local.entities.Restaurant

@Database(entities = [Restaurant::class, Food::class], version = 2)
@TypeConverters(Converters::class)
abstract class DbHelper: RoomDatabase() {

    abstract fun getDao(): RestaurantDao

}