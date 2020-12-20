package com.example.food2you_restaurantsonly

import android.app.Application
import com.example.food2you_restaurantsonly.data.local.RestaurantDao
import com.example.food2you_restaurantsonly.data.local.entities.Food
import com.example.food2you_restaurantsonly.data.remote.RestApi
import com.example.food2you_restaurantsonly.data.local.entities.Restaurant
import com.example.food2you_restaurantsonly.data.remote.requests.AccountRequest
import com.example.food2you_restaurantsonly.other.Resource
import com.example.food2you_restaurantsonly.other.checkForInternetConnection
import com.example.food2you_restaurantsonly.other.networkBoundResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Repository
@Inject constructor(
    private val api: RestApi,
    private val dao: RestaurantDao,
    private val context: Application
) {

    suspend fun register(email: String, password: String) = withContext(Dispatchers.IO) {
        try {
            val response = api.registerRestaurant(AccountRequest(email, password))
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

    fun getAllFood(): Flow<Resource<List<Food>>> {
        return networkBoundResource(
            query = {
                dao.getAllFood()
            },
            fetch = {

            },
            savedFetchResult = {

            },
            shouldFetch = {
                checkForInternetConnection(context)
            }
        )
    }

    suspend fun getRestaurantByOwner(owner: String) = dao.getRestaurantByOwner(owner)

    suspend fun getRestaurantById(id: String) = dao.getRestaurantById(id)

    suspend fun getFoodById(id: String) = dao.getFoodById(id)


}