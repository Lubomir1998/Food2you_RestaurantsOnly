package com.example.food2you_restaurantsonly.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food2you_restaurantsonly.Repository
import com.example.food2you_restaurantsonly.data.local.entities.Food
import com.example.food2you_restaurantsonly.data.local.entities.Restaurant
import kotlinx.coroutines.launch

class AddRestaurantViewModel @ViewModelInject constructor(private val repository: Repository): ViewModel() {


    fun saveRestaurant(restaurant: Restaurant) = viewModelScope.launch {
        repository.addRestaurant(restaurant)
    }

    fun saveFood(food: Food) = viewModelScope.launch {
        repository.addFood(food)
    }


}