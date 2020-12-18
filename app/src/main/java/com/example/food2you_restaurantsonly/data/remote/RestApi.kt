package com.example.food2you_restaurantsonly.data.remote

import com.example.food2you_restaurantsonly.data.local.entities.Food
import com.example.food2you_restaurantsonly.data.local.entities.Restaurant
import com.example.food2you_restaurantsonly.data.remote.requests.AccountRequest
import com.example.food2you_restaurantsonly.data.remote.requests.DeleteRestaurantRequest
import com.example.food2you_restaurantsonly.data.remote.responses.SimpleResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RestApi {

    @POST("/registerRestaurant")
    suspend fun registerRestaurant(@Body accountRequest: AccountRequest): Response<SimpleResponse>

    @POST("/loginRes")
    suspend fun loginRes(@Body accountRequest: AccountRequest): Response<SimpleResponse>

    @POST("/addRestaurant")
    suspend fun insertRestaurant(@Body restaurant: Restaurant): Response<ResponseBody>

    @POST("/addFood")
    suspend fun addFood(@Body food: Food): Response<ResponseBody>

    @POST("/deleteRestaurant")
    suspend fun deleteRestaurant(@Body deleteRestaurantRequest: DeleteRestaurantRequest): Response<ResponseBody>

}