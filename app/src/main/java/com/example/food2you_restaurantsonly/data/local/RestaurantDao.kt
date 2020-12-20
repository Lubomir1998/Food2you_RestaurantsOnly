package com.example.food2you_restaurantsonly.data.local

import androidx.room.*
import com.example.food2you_restaurantsonly.data.local.entities.Food
import com.example.food2you_restaurantsonly.data.local.entities.Restaurant
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET

@Dao
interface RestaurantDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(food: Food)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRestaurant(restaurant: Restaurant)

    @Query("SELECT * FROM food")
    fun getAllFood(): Flow<List<Food>>

    @Query("SELECT * FROM food WHERE id = :id")
    suspend fun getFoodById(id: String): Food?

    @Query("SELECT * FROM restaurant WHERE owner = :owner")
    suspend fun getRestaurantByOwner(owner: String): Restaurant?

    @Query("SELECT * FROM restaurant WHERE id = :id")
    suspend fun getRestaurantById(id: String): Restaurant?


}