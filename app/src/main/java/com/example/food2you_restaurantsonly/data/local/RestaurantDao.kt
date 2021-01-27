package com.example.food2you_restaurantsonly.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.food2you_restaurantsonly.data.local.entities.Food
import com.example.food2you_restaurantsonly.data.local.entities.Order
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order)

    @Query("DELETE FROM food WHERE id = :foodId")
    suspend fun deleteFood(foodId: String)

    @Query("SELECT * FROM food")
    fun getAllFood(): Flow<List<Food>>

    @Query("SELECT * FROM restaurant")
    fun getRestaurantOfOwner(): Flow<Restaurant>

    @Query("SELECT * FROM food WHERE id = :id")
    suspend fun getFoodById(id: String): Food?

    @Query("SELECT * FROM restaurant WHERE owner = :owner")
    suspend fun getRestaurantByOwner(owner: String): Restaurant?

    @Query("SELECT * FROM restaurant WHERE id = :id")
    suspend fun getRestaurantById(id: String): Restaurant?

    @Query("DELETE FROM food")
    suspend fun deleteAllFood()

    @Query("DELETE FROM restaurant")
    suspend fun deleteRestaurant()

    @Query("SELECT * FROM `order` WHERE status = 'Waiting'")
    fun getAllOrders(): Flow<List<Order>>

    @Query("DELETE FROM `order`")
    suspend fun deleteAllOrders()

    @Query("SELECT * FROM `order` WHERE id = :id")
    suspend fun getOrderById(id: String): Order?


}