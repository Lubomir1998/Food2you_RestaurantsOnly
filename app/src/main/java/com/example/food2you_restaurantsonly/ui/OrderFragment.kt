package com.example.food2you_restaurantsonly.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.food2you_restaurantsonly.R
import com.example.food2you_restaurantsonly.adapters.OrderAdapter
import com.example.food2you_restaurantsonly.data.local.entities.Order
import com.example.food2you_restaurantsonly.databinding.OrdersFragmentBinding
import com.example.food2you_restaurantsonly.other.Status
import com.example.food2you_restaurantsonly.viewmodels.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderFragment: Fragment(R.layout.orders_fragment) {

    private val viewModel: OrderViewModel by viewModels()
    private lateinit var binding: OrdersFragmentBinding
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var listener: OrderAdapter.OnOrderClickListener
    private val args: OrderFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = OrdersFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.show()

        listener = object : OrderAdapter.OnOrderClickListener {
            override fun onOrderClicked(order: Order) {
                val action = OrderFragmentDirections.actionOrderFragmentToSwitchOrderStatusFragment(order.id, args.restaurantName)
                findNavController().navigate(action)
            }
        }

        orderAdapter = OrderAdapter(listener)

        setupRecyclerView()
        subscribeToObservers()

    }




    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderAdapter
            setHasFixedSize(true)
        }
    }

    private fun subscribeToObservers() {
        viewModel.allOrders.observe(viewLifecycleOwner, {
            it?.let { event ->
                val result = event.peekContent()

                when(result.status) {
                    Status.SUCCESS -> {
                        val list = result.data!!

                        orderAdapter.orders = list
                    }
                }

            }
        })
    }

}