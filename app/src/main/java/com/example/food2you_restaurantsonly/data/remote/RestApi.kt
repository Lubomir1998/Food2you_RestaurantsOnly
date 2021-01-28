package com.example.food2you_restaurantsonly.data.remote

import com.example.food2you_restaurantsonly.data.local.entities.Food
import com.example.food2you_restaurantsonly.data.local.entities.Order
import com.example.food2you_restaurantsonly.data.local.entities.Restaurant
import com.example.food2you_restaurantsonly.data.remote.requests.AccountRequest
import com.example.food2you_restaurantsonly.data.remote.requests.DeleteFoodRequest
import com.example.food2you_restaurantsonly.data.remote.requests.DeleteRestaurantRequest
import com.example.food2you_restaurantsonly.data.remote.requests.UpdateOrderStatusRequest
import com.example.food2you_restaurantsonly.data.remote.responses.SimpleResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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

    @POST("/deleteFood")
    suspend fun deleteFood(@Body deleteFoodRequest: DeleteFoodRequest): Response<ResponseBody>

    @GET("/getFoodResOnly")
    suspend fun getFood(): Response<List<Food>>

    @GET("/getRestaurantOfOwner")
    suspend fun getRestaurantOfOwner(): Response<Restaurant>

    @GET("/allOrders")
    suspend fun getAllOrders(): Response<List<Order>>

    @POST("/updateOrderStatus")
    suspend fun updateOrderStatus(@Body request: UpdateOrderStatusRequest): Response<SimpleResponse>

    @POST("/registerOwnerToken/{owner}")
    suspend fun registerOwnerToken(@Body userToken: UserToken, @Path("owner") ownerEmail: String): Response<SimpleResponse>

    @POST("/changeRestaurantToken/{owner}")
    suspend fun changeRestaurantToken(@Body userToken: UserToken, @Path("owner") ownerEmail: String): Response<SimpleResponse>

}