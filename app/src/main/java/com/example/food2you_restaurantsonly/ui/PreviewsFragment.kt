package com.example.food2you_restaurantsonly.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.food2you_restaurantsonly.R
import com.example.food2you_restaurantsonly.adapters.PreviewsAdapter
import com.example.food2you_restaurantsonly.databinding.PreviewsFragmentBinding
import com.example.food2you_restaurantsonly.other.Constants.KEY_RES_ID
import com.example.food2you_restaurantsonly.other.Status
import com.example.food2you_restaurantsonly.viewmodels.AddRestaurantViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PreviewsFragment: Fragment(R.layout.previews_fragment) {

    private lateinit var binding: PreviewsFragmentBinding
    private val mAdapter = PreviewsAdapter()
    private val viewModel: AddRestaurantViewModel by viewModels()

    @Inject lateinit var sharedPrefs: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = PreviewsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.show()

        binding.recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        val restaurantId = sharedPrefs.getString(KEY_RES_ID, "") ?: ""

        if(restaurantId.isNotEmpty()) {
            viewModel.getRestaurantById(restaurantId)
            subscribeToObservers()
        }




    }

    private fun subscribeToObservers() {
        viewModel.restaurant.observe(viewLifecycleOwner, {
            it?.let { event ->
                val result = event.peekContent()

                when(result.status) {
                    Status.SUCCESS -> {
                        val restaurant = result.data

                        val previews = restaurant?.previews

                        previews?.let {
                            mAdapter.previews = it
                        }

                    }
                    Status.ERROR -> {
                        event.getContentIfNotHandled()?.let {
                            Snackbar.make(requireView(), it.message ?: "An error occurred", Snackbar.LENGTH_LONG).show()
                        }
                    }
                    Status.LOADING -> { }
                }

            }
        })
    }

}