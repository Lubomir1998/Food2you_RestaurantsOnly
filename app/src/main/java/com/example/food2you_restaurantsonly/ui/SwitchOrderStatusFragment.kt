package com.example.food2you_restaurantsonly.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.food2you_restaurantsonly.R
import com.example.food2you_restaurantsonly.data.local.entities.Order
import com.example.food2you_restaurantsonly.data.remote.NotificationData
import com.example.food2you_restaurantsonly.data.remote.PushNotification
import com.example.food2you_restaurantsonly.databinding.SwitchOrderStatusFragmentBinding
import com.example.food2you_restaurantsonly.other.Status
import com.example.food2you_restaurantsonly.viewmodels.OrderViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SwitchOrderStatusFragment: Fragment(R.layout.switch_order_status_fragment) {

    private lateinit var binding: SwitchOrderStatusFragmentBinding
    private val viewModel: OrderViewModel by viewModels()
    private val navArgs: SwitchOrderStatusFragmentArgs by navArgs()
    private var currentOrder: Order? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SwitchOrderStatusFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.show()

        if(navArgs.orderId.isNotEmpty()) {
            viewModel.getOrderById(navArgs.orderId)
            subscribeToObservers()
        }

        binding.switchStatus.setOnClickListener {
            if(navArgs.orderId.isNotEmpty()) {
                viewModel.updateOrderStatus(navArgs.orderId, "On the way")
                observeOrderStatus()
            }
            binding.switchStatus.isClickable = false
        }

    }



    private fun sendPushNotificationToUser() {
        val title = if(navArgs.restaurantName.isNotEmpty()) {
            navArgs.restaurantName
        }
        else {
            "On the way!"
        }
        viewModel.sendPushNotification(PushNotification(NotificationData(title, "Your order from ${navArgs.restaurantName} is on the way!"), currentOrder!!.recipient))
    }


    private fun subscribeToObservers() {
        viewModel.order.observe(viewLifecycleOwner, {
            it?.let { event ->
                val result = event.peekContent()

                when(result.status) {
                    Status.SUCCESS -> {
                        val order = result.data!!
                        currentOrder = order
                    }
                    else -> {}
                }
            }
        })

    }

    private fun observeOrderStatus() {
        viewModel.updateOrderStatus.observe(viewLifecycleOwner, {
            it?.let { event ->

                val result = event.peekContent()

                when(result.status) {
                    Status.SUCCESS -> {
                        binding.progressBarOrder.visibility = View.GONE

                        Snackbar.make(requireView(), result.message ?: "Status updated successfully", Snackbar.LENGTH_LONG).show()
                        sendPushNotificationToUser()
                    }
                    Status.ERROR -> {
                        event.getContentIfNotHandled()?.let {
                            binding.progressBarOrder.visibility = View.GONE
                            Snackbar.make(
                                requireView(),
                                it.message ?: "An error occurred",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                    Status.LOADING -> {
                        binding.progressBarOrder.visibility = View.VISIBLE
                    }
                }

            }
        })
    }

}