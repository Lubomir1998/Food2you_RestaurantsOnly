package com.example.food2you_restaurantsonly.ui

import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.food2you_restaurantsonly.R
import com.example.food2you_restaurantsonly.databinding.AddRestaurantFragmentBinding
import com.example.food2you_restaurantsonly.databinding.LoginFragmentBinding
import com.example.food2you_restaurantsonly.databinding.MyRestaurantsFragmentBinding
import com.example.food2you_restaurantsonly.other.Constants.KEY_EMAIL
import com.example.food2you_restaurantsonly.other.Constants.KEY_PASSWORD
import com.example.food2you_restaurantsonly.other.Constants.NO_EMAIL
import com.example.food2you_restaurantsonly.other.Constants.NO_PASSWORD
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyRestaurantsFragment: Fragment(R.layout.my_restaurants_fragment) {

    private lateinit var binding: MyRestaurantsFragmentBinding

    @Inject
    lateinit var sharedPrefs: SharedPreferences

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

        binding.addRestaurantImg.setOnClickListener {
            findNavController().navigate(R.id.action_myRestaurantsFragment_to_addRestaurantFragment)
        }

    }






    private fun logOut() {
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