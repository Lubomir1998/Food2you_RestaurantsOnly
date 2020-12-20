package com.example.food2you_restaurantsonly.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.food2you_restaurantsonly.R
import com.example.food2you_restaurantsonly.data.local.entities.Food
import com.example.food2you_restaurantsonly.databinding.AddFoodFragmentBinding
import com.example.food2you_restaurantsonly.other.Constants.KEY_EMAIL
import com.example.food2you_restaurantsonly.other.Constants.NO_EMAIL
import com.example.food2you_restaurantsonly.other.checkForInternetConnection
import com.example.food2you_restaurantsonly.viewmodels.AddRestaurantViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AddMealFragment: Fragment(R.layout.add_food_fragment) {

    private lateinit var binding: AddFoodFragmentBinding
    private val model: AddRestaurantViewModel by viewModels()

    private val args: AddMealFragmentArgs by navArgs()

    @Inject
    lateinit var sharedPrefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddFoodFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if(args.id.isNotEmpty()) {
            model.getFoodById(args.id)
            // subscribe to observers
        }


        binding.saveFoodImg.visibility = if(areFieldsFilledOut()) {
            View.VISIBLE
        }
        else {
            View.GONE
        }




        binding.foodNameEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.saveFoodImg.visibility = if(areFieldsFilledOut()) {
                    View.VISIBLE
                }
                else {
                    View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) { }
        })

        binding.foodTypeEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.saveFoodImg.visibility = if(areFieldsFilledOut()) {
                    View.VISIBLE
                }
                else {
                    View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) { }
        })

        binding.foodPriceEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.saveFoodImg.visibility = if(areFieldsFilledOut()) {
                    View.VISIBLE
                }
                else {
                    View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) { }
        })

        binding.foodWeightEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.saveFoodImg.visibility = if(areFieldsFilledOut()) {
                    View.VISIBLE
                }
                else {
                    View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) { }
        })

        binding.urlEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.saveFoodImg.visibility = if(areFieldsFilledOut()) {
                    View.VISIBLE
                }
                else {
                    View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) { }
        })


        binding.saveFoodImg.setOnClickListener {
            val mealName = binding.foodNameEt.text.toString()
            val type = binding.foodTypeEt.text.toString()
            val weight = binding.foodWeightEt.text.toString().toInt()
            val price = binding.foodPriceEt.text.toString().toFloat()
            val imgUrl = binding.urlEt.text.toString()
            val id = UUID.randomUUID().toString()

            val owner = sharedPrefs.getString(KEY_EMAIL, NO_EMAIL) ?: NO_EMAIL

            val food = Food(mealName, type, weight, imgUrl, price, owner, id = id)

            if(checkForInternetConnection(requireContext())) {
                if (owner != NO_EMAIL) {
                    model.saveFood(food)
                    Snackbar.make(requireView(), "Meal Saved", Snackbar.LENGTH_LONG).show()
                    clearTextFields()
                } else {
                    Snackbar.make(requireView(), "An unknown error occurred", Snackbar.LENGTH_LONG)
                        .show()
                }
            }
            else {
                Snackbar.make(requireView(), "Check your internet connection", Snackbar.LENGTH_LONG).show()
            }
        }

    }







    private fun areFieldsFilledOut(): Boolean {
        return binding.foodNameEt.text.toString().isNotEmpty()
                && binding.foodTypeEt.text.toString().isNotEmpty()
                && binding.foodWeightEt.text.toString().isNotEmpty()
                && binding.foodPriceEt.text.toString().isNotEmpty()
                && binding.urlEt.text.toString().isNotEmpty()
    }

    private fun clearTextFields() {
        binding.foodNameEt.text?.clear()
        binding.foodTypeEt.text?.clear()
        binding.foodWeightEt.text?.clear()
        binding.foodPriceEt.text?.clear()
        binding.urlEt.text?.clear()
    }

}