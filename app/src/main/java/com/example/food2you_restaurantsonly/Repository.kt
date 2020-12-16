package com.example.food2you_restaurantsonly

import com.example.food2you_restaurantsonly.data.RestApi
import com.example.food2you_restaurantsonly.data.requests.AccountRequest
import com.example.food2you_restaurantsonly.other.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Repository @Inject constructor(private val api: RestApi) {

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

}