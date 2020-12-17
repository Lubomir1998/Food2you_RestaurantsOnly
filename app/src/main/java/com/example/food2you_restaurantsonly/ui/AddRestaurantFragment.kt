package com.example.food2you_restaurantsonly.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.food2you_restaurantsonly.R
import com.example.food2you_restaurantsonly.databinding.AddRestaurantFragmentBinding
import com.example.food2you_restaurantsonly.databinding.LoginFragmentBinding

class AddRestaurantFragment: Fragment(R.layout.add_restaurant_fragment) {

    private lateinit var binding: AddRestaurantFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddRestaurantFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backArrowImg.setOnClickListener {
            findNavController().navigate(R.id.action_addRestaurantFragment_to_myRestaurantsFragment)
        }

    }

}