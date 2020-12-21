package com.example.food2you_restaurantsonly.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.food2you_restaurantsonly.FoodAdapter
import com.example.food2you_restaurantsonly.R
import com.example.food2you_restaurantsonly.data.local.entities.Food
import com.example.food2you_restaurantsonly.data.local.entities.Restaurant
import com.example.food2you_restaurantsonly.databinding.AddRestaurantFragmentBinding
import com.example.food2you_restaurantsonly.other.Constants.KEY_EMAIL
import com.example.food2you_restaurantsonly.other.Constants.NO_EMAIL
import com.example.food2you_restaurantsonly.other.Status
import com.example.food2you_restaurantsonly.other.checkForInternetConnection
import com.example.food2you_restaurantsonly.viewmodels.AddRestaurantViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

private const val TAG = "AddRestaurantFragment"
@AndroidEntryPoint
class AddRestaurantFragment: Fragment(R.layout.add_restaurant_fragment) {

    private lateinit var binding: AddRestaurantFragmentBinding
    private val model: AddRestaurantViewModel by viewModels()
    private val args: AddRestaurantFragmentArgs by navArgs()
    @Inject
    lateinit var sharedPrefs: SharedPreferences
    private var currentRestaurant: Restaurant? = null
    private lateinit var foodAdapter: FoodAdapter
    private lateinit var listener: FoodAdapter.OnFoodClickListener

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

        if(args.resId.isNotEmpty()) {
            Log.d(TAG, "***********second page onViewCreated: ${args.resId}")
            model.getRestaurantById(args.resId)
            subscribeToObservers()
        }

        listener = object : FoodAdapter.OnFoodClickListener {
            override fun onFoodClicked(food: Food) {
                val action = AddRestaurantFragmentDirections.actionAddRestaurantFragmentToAddMealFragment(food.id)
                findNavController().navigate(action)
            }

            override fun deleteFood(food: Food) {
                if(checkForInternetConnection(requireContext())){
                    model.deleteFood(food.id)
                    Snackbar.make(requireView(), "Deleted", Snackbar.LENGTH_LONG)
                        .setAction("Undo") {
                            if(checkForInternetConnection(requireContext())) {
                                model.saveFood(food)
                            }
                            else {
                                Snackbar.make(requireView(), "Check your internet connection", Snackbar.LENGTH_LONG).show()
                            }
                        }
                        .show()
                }
                else {
                    Snackbar.make(requireView(), "Check your internet connection", Snackbar.LENGTH_LONG).show()
                }
            }
        }

        foodAdapter = FoodAdapter(listOf(), requireContext(), listener)

        setUpRecyclerView()
        allFoodObserver()


        binding.addBtn.setOnClickListener {
            if(areFieldsFilledOut()) {
                val name = binding.restaurantNameEt.text.toString()
                val type = binding.restaurantTypeEt.text.toString()
                val kitchen = binding.restaurantKitchenEt.text.toString()
                val deliveryPrice = binding.restaurantDeliveryPriceEt.text.toString().toFloat()
                val minutes = binding.restaurantDeliveryTimeEt.text.toString().toInt()
                val minPrice = binding.restaurantMinimumPriceEt.text.toString().toFloat()
                val imgUrl = binding.urlEt.text.toString()
                val owner = sharedPrefs.getString(KEY_EMAIL, NO_EMAIL) ?: NO_EMAIL
                val id = currentRestaurant?.id ?: UUID.randomUUID().toString()

                val restaurant = Restaurant(name, type, kitchen, deliveryPrice, minutes, minPrice, imgUrl, listOf(), listOf(), owner, id = id)

                if(checkForInternetConnection(requireContext())) {
                    if(owner != NO_EMAIL) {
                        model.saveRestaurant(restaurant)
                       // Snackbar.make(requireView(), "Restaurant Saved", Snackbar.LENGTH_LONG).show()
                    }
                    else {
                        Snackbar.make(requireView(), "An unknown error occurred", Snackbar.LENGTH_LONG).show()
                    }
                }
                else {
                    Snackbar.make(requireView(), "Check your internet connection", Snackbar.LENGTH_LONG).show()
                }

            }
        }


        binding.addFoodBtn.setOnClickListener {
            findNavController().navigate(R.id.action_addRestaurantFragment_to_addMealFragment)
        }

    }




    private fun displayData(list: List<Food>) {
        foodAdapter.foodList = list
        foodAdapter.notifyDataSetChanged()
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.apply {
            adapter = foodAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun allFoodObserver() {
        model.allFood.observe(viewLifecycleOwner, {
            it?.let { event ->
                val result = event.peekContent()

                when (result.status) {
                    Status.SUCCESS -> {
                        displayData(result.data!!)
                        Log.d(TAG, "%********allFoodObserver: ${result.data.size}")
                    }
                    Status.ERROR -> {
                        event.getContentIfNotHandled()?.let { error ->
                            error.message?.let { message ->
                                Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
                            }
                        }
                        result.data?.let { notes ->
                            displayData(notes)
                        }
                    }
                    Status.LOADING -> {
                        result.data?.let { notes ->
                            displayData(notes)
                        }
                    }
                }
            }
        })
    }

    private fun subscribeToObservers() {
        model.restaurant.observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { result ->

                when(result.status) {
                    Status.SUCCESS -> {
                        val restaurant = result.data!!
                        currentRestaurant = restaurant

                        binding.restaurantNameEt.setText(currentRestaurant!!.name)
                        binding.restaurantTypeEt.setText(currentRestaurant!!.type)
                        binding.restaurantKitchenEt.setText(currentRestaurant!!.kitchen)
                        binding.urlEt.setText(currentRestaurant!!.imgUrl)
                        binding.restaurantMinimumPriceEt.setText(currentRestaurant!!.minimalPrice.toString())
                        binding.restaurantDeliveryPriceEt.setText(currentRestaurant!!.deliveryPrice.toString())
                        binding.restaurantDeliveryTimeEt.setText(currentRestaurant!!.deliveryTimeMinutes.toString())

                    }
                    Status.ERROR -> {

                    } Status.LOADING -> {

                }
                }

            }
        })
    }


    private fun areFieldsFilledOut(): Boolean {
        return binding.restaurantNameEt.text.toString().isNotEmpty()
                && binding.restaurantTypeEt.text.toString().isNotEmpty()
                && binding.restaurantKitchenEt.text.toString().isNotEmpty()
                && binding.urlEt.text.toString().isNotEmpty()
                && binding.restaurantDeliveryPriceEt.text.toString().isNotEmpty()
                && binding.restaurantDeliveryTimeEt.text.toString().isNotEmpty()
                && binding.restaurantMinimumPriceEt.text.toString().isNotEmpty()
    }

    private fun clearTextFields() {
        binding.restaurantNameEt.text?.clear()
        binding.restaurantTypeEt.text?.clear()
        binding.restaurantKitchenEt.text?.clear()
        binding.urlEt.text?.clear()
        binding.restaurantDeliveryPriceEt.text?.clear()
        binding.restaurantDeliveryTimeEt.text?.clear()
        binding.restaurantMinimumPriceEt.text?.clear()
    }

}