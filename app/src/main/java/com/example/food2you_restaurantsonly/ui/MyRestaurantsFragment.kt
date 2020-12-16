package com.example.food2you_restaurantsonly.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.food2you_restaurantsonly.R
import com.example.food2you_restaurantsonly.databinding.AddRestaurantFragmentBinding
import com.example.food2you_restaurantsonly.databinding.LoginFragmentBinding
import com.example.food2you_restaurantsonly.databinding.MyRestaurantsFragmentBinding

class MyRestaurantsFragment: Fragment(R.layout.my_restaurants_fragment) {

    private lateinit var binding: MyRestaurantsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MyRestaurantsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}