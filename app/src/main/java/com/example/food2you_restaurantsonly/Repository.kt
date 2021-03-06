package com.example.food2you_restaurantsonly

import android.app.Application
import com.example.food2you_restaurantsonly.data.local.RestaurantDao
import com.example.food2you_restaurantsonly.data.local.entities.Food
import com.example.food2you_restaurantsonly.data.local.entities.Order
import com.example.food2you_restaurantsonly.data.remote.RestApi
import com.example.food2you_restaurantsonly.data.local.entities.Restaurant
import com.example.food2you_restaurantsonly.data.remote.FirebaseApi
import com.example.food2you_restaurantsonly.data.remote.PushNotification
import com.example.food2you_restaurantsonly.data.remote.UserToken
import com.example.food2you_restaurantsonly.data.remote.requests.AccountRequest
import com.example.food2you_restaurantsonly.data.remote.requests.DeleteFoodRequest
import com.example.food2you_restaurantsonly.data.remote.requests.RegisterUserRequest
import com.example.food2you_restaurantsonly.data.remote.requests.UpdateOrderStatusRequest
import com.example.food2you_restaurantsonly.other.Resource
import com.example.food2you_restaurantsonly.other.checkForInternetConnection
import com.example.food2you_restaurantsonly.other.networkBoundResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class Repository
@Inject constructor(
    private val api: RestApi,
    private val dao: RestaurantDao,
    private val context: Application,
    private val firebaseApi: FirebaseApi
) {

    suspend fun register(email: String, password: String, token: String) = withContext(Dispatchers.IO) {
        try {
            val response = api.registerRestaurant(RegisterUserRequest(email, password, token))
            if(response.isSuccessful && response.body()!!.isSuccessful) {
                Resource.success(response.body()?.message)
            }
            else {
                Resource.error( response.body()?.message ?: response.message(), null)
            }
        }
        catch (e: Exception) {
            Resource.error( "Couldn't connect to servers. Check your internet connection", null)
        }
    }

    suspend fun login(email: String, password: String) = withContext(Dispatchers.IO) {
        try {
            val response = api.loginRes(AccountRequest(email, password))
            if(response.isSuccessful && response.body()!!.isSuccessful) {
                Resource.success(response.body()?.message)
            }
            else {
                Resource.error( response.body()?.message ?: response.message(), null)
            }
        }
        catch (e: Exception) {
            Resource.error( "Couldn't connect to servers. Check your internet connection", null)
        }
    }

    suspend fun addRestaurant(restaurant: Restaurant) {
        val response = try {
            api.insertRestaurant(restaurant)
        } catch (e: Exception) {
            null
        }

        if(response != null) {
            dao.insertRestaurant(restaurant)
        }

    }

    suspend fun addFood(food: Food) {
        val response = try {
            api.addFood(food)
        } catch (e: Exception) {
            null
        }

        if(response != null) {
            dao.insertFood(food)
        }

    }

    suspend fun deleteFoodById(foodId: String) {
        val response = try {
            api.deleteFood(DeleteFoodRequest(foodId))
        } catch (e: Exception) {
            null
        }

        if(response != null) {
            dao.deleteFood(foodId)
        }

    }

    private var currentResponse: Response<List<Food>>? = null

    private suspend fun sync() {
        currentResponse = api.getFood()
        currentResponse?.body()?.let { meals ->
            dao.deleteAllFood()
            meals.forEach { addFood(it) }
        }
    }

    suspend fun deleteAllFood() = dao.deleteAllFood()
    suspend fun deleteAllRestaurant() = dao.deleteRestaurant()

    fun getAllFood(): Flow<Resource<List<Food>>> {
        return networkBoundResource(
            query = {
                dao.getAllFood()
            },
            fetch = {
                sync()
                currentResponse
            },
            savedFetchResult = { response ->
                response?.body()?.let { meals ->
                    meals.forEach { addFood(it) }
                }
            },
            shouldFetch = {
                checkForInternetConnection(context)
            }
        )
    }

    private var currentResponseRestaurants: Response<Restaurant>? = null

    private suspend fun syncCurrentRes() {
        currentResponseRestaurants = api.getRestaurantOfOwner()
        currentResponseRestaurants?.body()?.let { restaurant ->
            dao.deleteRestaurant()
            addRestaurant(restaurant)
        }
    }

    fun getRestaurant(): Flow<Resource<Restaurant>> {
        return networkBoundResource(
                query = {
                    dao.getRestaurantOfOwner()
                },
                fetch = {
                    syncCurrentRes()
                    currentResponseRestaurants
                },
                savedFetchResult = { response ->
                    response?.body()?.let { restaurant ->
                        addRestaurant(restaurant)
                    }
                },
                shouldFetch = {
                    checkForInternetConnection(context)
                }
        )
    }

    suspend fun getRestaurantByOwner(owner: String) = dao.getRestaurantByOwner(owner)


    suspend fun getRestaurantById(id: String) = dao.getRestaurantById(id)

    suspend fun getFoodById(id: String) = dao.getFoodById(id)

    suspend fun getOrderById(id: String) = dao.getOrderById(id)

    private var currentOrderResponse: Response<List<Order>>? = null

    private suspend fun syncOrders() {
        currentOrderResponse = api.getAllOrders()
        currentOrderResponse?.body()?.let { orders ->
            dao.deleteAllOrders()
            for(order in orders) {
                dao.insertOrder(order)
            }
        }
    }

    fun getOrders(): Flow<Resource<List<Order>>> {
        return networkBoundResource(
            query = {
                dao.getAllOrders()
            },
            fetch = {
                syncOrders()
                currentOrderResponse
            },
            savedFetchResult = { response ->
                response?.body()?.let { orders ->
                    orders.forEach { dao.insertOrder(it) }

                }
            },
            shouldFetch = {
                checkForInternetConnection(context)
            }
        )
    }

    suspend fun updateOrderStatus(id: String, status: String) = withContext(Dispatchers.IO) {

        val response = api.updateOrderStatus(UpdateOrderStatusRequest(id, status))

        try {
            if(response.isSuccessful && response.body()!!.isSuccessful) {
                Resource.success(response.body()?.message)
            }
            else {
                Resource.error( response.body()?.message ?: response.message(), null)
            }
        }
        catch (e: Exception) {
            Resource.error( "Couldn't connect to servers. Check your internet connection", null)
        }


    }

    // Firebase
    suspend fun sendPushNotification(pushNotification: PushNotification) {
        try {
            firebaseApi.postNotification(pushNotification)
        } catch (e: Exception) { }
    }

    suspend fun registerOwnerToken(userToken: UserToken, email: String) = api.registerOwnerToken(userToken, email)

    suspend fun changeRestaurantToken(userToken: UserToken, email: String) = api.changeRestaurantToken(userToken, email)


}