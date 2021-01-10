package com.example.food2you_restaurantsonly.ui

import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.food2you_restaurantsonly.R
import com.example.food2you_restaurantsonly.data.local.entities.Restaurant
import com.example.food2you_restaurantsonly.databinding.MyRestaurantsFragmentBinding
import com.example.food2you_restaurantsonly.other.Constants.KEY_EMAIL
import com.example.food2you_restaurantsonly.other.Constants.KEY_PASSWORD
import com.example.food2you_restaurantsonly.other.Constants.NO_EMAIL
import com.example.food2you_restaurantsonly.other.Constants.NO_PASSWORD
import com.example.food2you_restaurantsonly.other.Status
import com.example.food2you_restaurantsonly.viewmodels.AddRestaurantViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.security.acl.NotOwnerException
import javax.inject.Inject

private const val TAG = "MyRestaurantsFragment"
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
        setHasOptionsMenu(true)
        binding = MyRestaurantsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER

        if(sharedPrefs.getString(KEY_EMAIL, NO_EMAIL) != NO_EMAIL) {
            val email = sharedPrefs.getString(KEY_EMAIL, NO_EMAIL) ?: ""
            model.getRestaurantByOwner(sharedPrefs.getString(KEY_EMAIL, "") ?: "")
            binding.emailTV.text = "Welcome $email"
            subscribeToObservers()
        }


        binding.addRestaurantImg.setOnClickListener {
            val action = MyRestaurantsFragmentDirections.actionMyRestaurantsFragmentToAddRestaurantFragment(currentRestaurant?.id ?: "")
            Log.d(TAG, "********clicked: ${currentRestaurant?.id}")
            findNavController().navigate(action)
        }

    }




    private fun subscribeToObservers() {
        model.restaurantOfOwner.observe(viewLifecycleOwner, {
            it?.let { event ->

                val result = event.peekContent()

                when (result.status) {
                    Status.SUCCESS -> {
                        val restaurant = result.data
                        currentRestaurant = restaurant

                        Picasso.with(requireContext()).load(currentRestaurant?.imgUrl).into(binding.resImg)
                        binding.resNameTextView.text = currentRestaurant?.name

                        Log.d(TAG, "********subscribeToObservers: ${currentRestaurant?.name}")
                    }
                    Status.ERROR -> {


                    }
                    Status.LOADING -> {

                    }
                }

            }
        })
    }

    private fun logOut() {

        model.deleteFood()
        model.deleteRestaurant()

        sharedPrefs.edit()
            .putString(KEY_EMAIL, NO_EMAIL)
            .putString(KEY_PASSWORD, NO_PASSWORD)
            .apply()

        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.myRestaurantsFragment, true)
            .build()

        findNavController().navigate(MyRestaurantsFragmentDirections.actionMyRestaurantsFragmentToLoginFragment(), navOptions)
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_restaurants, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.miLogout -> logOut()
        }

        return super.onOptionsItemSelected(item)
    }

}