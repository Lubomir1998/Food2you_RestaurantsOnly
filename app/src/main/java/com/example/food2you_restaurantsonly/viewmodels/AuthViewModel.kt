package com.example.food2you_restaurantsonly.viewmodels

import androidx.core.text.isDigitsOnly
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food2you_restaurantsonly.Repository
import com.example.food2you_restaurantsonly.other.Resource
import kotlinx.coroutines.launch

class AuthViewModel @ViewModelInject constructor(private val repository: Repository): ViewModel() {

    private val _registerStatus = MutableLiveData<Resource<String>>()
    val registerStatus: LiveData<Resource<String>> = _registerStatus

    fun registerRestaurantOwner(email: String, password: String, confirmPassword: String) {
        _registerStatus.postValue(Resource.loading(null))

        if(email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            _registerStatus.postValue(Resource.error("All fields must be filled", null))
            return
        }

        if(password != confirmPassword) {
            _registerStatus.postValue(Resource.error("Passwords do not match", null))
            return
        }

        if(password.length < 6 || password.length > 18) {
            _registerStatus.postValue(Resource.error("Password must be between 6 and 18 characters", null))
            return
        }
        if(password.isDigitsOnly()) {
            _registerStatus.postValue(Resource.error("Password must contain at least one letter", null))
            return
        }
        if(!(password.matches(".*\\d+.*".toRegex()))) {
            _registerStatus.postValue(Resource.error("Password must contain at least one number", null))
            return
        }
        viewModelScope.launch {
            val result = repository.register(email, password)
            _registerStatus.postValue(result)
        }

    }


}