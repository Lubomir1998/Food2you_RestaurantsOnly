package com.example.food2you_restaurantsonly.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.food2you_restaurantsonly.R
import com.example.food2you_restaurantsonly.databinding.LoginFragmentBinding
import com.example.food2you_restaurantsonly.databinding.RegisterFragmentBinding
import com.example.food2you_restaurantsonly.other.Status
import com.example.food2you_restaurantsonly.viewmodels.AuthViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment: Fragment(R.layout.register_fragment) {

    private lateinit var binding: RegisterFragmentBinding
    private val model: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RegisterFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToObservers()

        binding.clickHereBtn.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }


        binding.regButton.setOnClickListener {
            val email = binding.userNameEt.text.toString()
            val password = binding.passwordEt.text.toString()
            val confirmPassword = binding.confirmPasswordEt.text.toString()

            model.registerRestaurantOwner(email, password, confirmPassword)
        }


    }

    private fun subscribeToObservers() {
        model.registerStatus.observe(viewLifecycleOwner, {
            it?.let { result ->
                when(result.status) {
                    Status.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        Snackbar.make(requireView(), "Successfully created account", Snackbar.LENGTH_LONG).show()
                    }
                    Status.ERROR -> {
                        binding.progressBar.visibility = View.GONE
                        Snackbar.make(requireView(), result.message ?: "An unknown error occured", Snackbar.LENGTH_LONG).show()

                    }
                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }

            }
        })
    }

}