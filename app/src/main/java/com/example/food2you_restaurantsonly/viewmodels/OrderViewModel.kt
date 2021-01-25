package com.example.food2you_restaurantsonly.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.food2you_restaurantsonly.Repository
import com.example.food2you_restaurantsonly.data.local.entities.Order
import com.example.food2you_restaurantsonly.data.remote.PushNotification
import com.example.food2you_restaurantsonly.other.Event
import com.example.food2you_restaurantsonly.other.Resource
import kotlinx.coroutines.launch

class OrderViewModel @ViewModelInject constructor(private val repository: Repository): ViewModel() {

    private val _forceUpdate = MutableLiveData(false)

    private val _allOrders = _forceUpdate.switchMap {
        repository.getOrders().asLiveData(viewModelScope.coroutineContext)
    }.switchMap {
        MutableLiveData(Event(it))
    }

    val allOrders = _allOrders



    private val _order = MutableLiveData<Event<Resource<Order>>>()
    val order: LiveData<Event<Resource<Order>>> = _order

    fun getOrderById(id: String) = viewModelScope.launch {
        val order = repository.getOrderById(id)

        order?.let {
            _order.postValue(Event(Resource.success(it)))
        } ?: _order.postValue(Event(Resource.error("error", null)))

    }


    private val _updateOrderStatus = MutableLiveData<Event<Resource<String>>>()
    val updateOrderStatus: LiveData<Event<Resource<String>>> = _updateOrderStatus

    fun updateOrderStatus(orderId: String, newStatus: String) {
        _updateOrderStatus.postValue(Event(Resource.loading(null)))

        viewModelScope.launch {
            val result = repository.updateOrderStatus(orderId, newStatus)
            _updateOrderStatus.postValue(Event(result))
        }
    }

    fun sendPushNotification(pushNotification: PushNotification) = viewModelScope.launch {
        repository.sendPushNotification(pushNotification)
    }

}