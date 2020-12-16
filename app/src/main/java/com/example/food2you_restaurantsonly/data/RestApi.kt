package com.example.food2you_restaurantsonly.data

import com.example.food2you_restaurantsonly.data.entities.Restaurant
import com.example.food2you_restaurantsonly.data.requests.AccountRequest
import com.example.food2you_restaurantsonly.data.requests.DeleteRestaurantRequest
import com.example.food2you_restaurantsonly.data.responses.SimpleResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RestApi {

    @POST("/registerRestaurant")
    suspend fun registerRestaurant(@Body accountRequest: AccountRequest): Response<SimpleResponse>

    @POST("/addRestaurant")
    suspend fun insertRestaurant(@Body restaurant: Restaurant): Response<ResponseBody>

    @POST("/deleteRestaurant")
    suspend fun deleteRestaurant(@Body deleteRestaurantRequest: DeleteRestaurantRequest): Response<ResponseBody>

}