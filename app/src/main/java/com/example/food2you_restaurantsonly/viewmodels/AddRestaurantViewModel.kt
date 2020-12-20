package com.example.food2you_restaurantsonly.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food2you_restaurantsonly.Repository
import com.example.food2you_restaurantsonly.data.local.entities.Food
import com.example.food2you_restaurantsonly.data.local.entities.Restaurant
import com.example.food2you_restaurantsonly.other.Event
import com.example.food2you_restaurantsonly.other.Resource
import kotlinx.coroutines.launch

class AddRestaurantViewModel @ViewModelInject constructor(private val repository: Repository): ViewModel() {


    private val _food = MutableLiveData<Event<Resource<Food>>>()
    val food: LiveData<Event<Resource<Food>>> = _food

    private val _restaurant = MutableLiveData<Event<Resource<Restaurant>>>()
    val restaurant: LiveData<Event<Resource<Restaurant>>> = _restaurant


    fun getFoodById(id: String) = viewModelScope.launch {
        _food.postValue(Event(Resource.loading(null)))
        val foods = repository.getFoodById(id)

        foods?.let {
            _food.postValue(Event(Resource.success(it)))
        } ?: _food.postValue(Event(Resource.error("Meal not found", null)))
    }

    fun getRestaurantByOwner(owner: String) = viewModelScope.launch {
        _restaurant.postValue(Event(Resource.loading(null)))
        val res = repository.getRestaurantByOwner(owner)

        res?.let {
            _restaurant.postValue(Event(Resource.success(it)))
        } ?: _restaurant.postValue(Event(Resource.error("Restaurant not found", null)))
    }

    fun getRestaurantById(id: String) = viewModelScope.launch {
        _restaurant.postValue(Event(Resource.loading(null)))
        val res = repository.getRestaurantById(id)

        res?.let {
            _restaurant.postValue(Event(Resource.success(it)))
        } ?: _restaurant.postValue(Event(Resource.error("Restaurant not found", null)))
    }




    fun saveRestaurant(restaurant: Restaurant) = viewModelScope.launch {
        repository.addRestaurant(restaurant)
    }

    fun saveFood(food: Food) = viewModelScope.launch {
        repository.addFood(food)
    }


}