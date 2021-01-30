package com.example.food2you_restaurantsonly.ui

import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.food2you_restaurantsonly.R
import com.example.food2you_restaurantsonly.data.local.entities.Restaurant
import com.example.food2you_restaurantsonly.databinding.MyRestaurantsFragmentBinding
import com.example.food2you_restaurantsonly.other.Constants.KEY_EMAIL
import com.example.food2you_restaurantsonly.other.Constants.KEY_NAME
import com.example.food2you_restaurantsonly.other.Constants.KEY_PASSWORD
import com.example.food2you_restaurantsonly.other.Constants.KEY_RES_ID
import com.example.food2you_restaurantsonly.other.Constants.NO_EMAIL
import com.example.food2you_restaurantsonly.other.Constants.NO_PASSWORD
import com.example.food2you_restaurantsonly.other.Status
import com.example.food2you_restaurantsonly.viewmodels.AddRestaurantViewModel
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyRestaurantsFragment: Fragment(R.layout.my_restaurants_fragment) {

    private lateinit var binding: MyRestaurantsFragmentBinding
    private val model: AddRestaurantViewModel by viewModels()

    @Inject
    lateinit var sharedPrefs: SharedPreferences


    private var currentRestaurant: Restaurant? = null

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

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER

        (activity as AppCompatActivity).supportActionBar?.show()

        if(sharedPrefs.getString(KEY_EMAIL, NO_EMAIL) != NO_EMAIL) {
            val email = sharedPrefs.getString(KEY_EMAIL, NO_EMAIL) ?: ""
            model.getRestaurantByOwner(sharedPrefs.getString(KEY_EMAIL, "") ?: "")
            binding.emailTV.text = "Welcome $email"
            subscribeToObservers()
        }


//        binding.addRestaurantImg.setOnClickListener {
//            val action = MyRestaurantsFragmentDirections.actionMyRestaurantsFragmentToAddRestaurantFragment(currentRestaurant?.id ?: "")
//            findNavController().navigate(action)
//        }
//
//        binding.resImg.setOnClickListener {
//            val action = MyRestaurantsFragmentDirections.actionMyRestaurantsFragmentToOrderFragment(currentRestaurant?.name ?: "")
//            findNavController().navigate(action)
//        }

    }




    private fun subscribeToObservers() {
        model.restaurantOfOwner.observe(viewLifecycleOwner, {
            it?.let { event ->

                val result = event.peekContent()

                when (result.status) {
                    Status.SUCCESS -> {
                        val restaurant = result.data
                        currentRestaurant = restaurant

                        sharedPrefs.edit().putString(KEY_RES_ID, currentRestaurant?.id).apply()
                        sharedPrefs.edit().putString(KEY_NAME, currentRestaurant?.name).apply()

                        Picasso.with(requireContext()).load(currentRestaurant?.imgUrl).into(binding.resImg)
                        binding.resNameTextView.text = currentRestaurant?.name

                    }
                    Status.ERROR -> {
                        event.getContentIfNotHandled()?.let {
                            Snackbar.make(requireView(), it.message ?: "An error occurred", Snackbar.LENGTH_LONG).show()
                        }

                    }
                    Status.LOADING -> {  }
                }

            }
        })
    }



}