package com.example.food2you_restaurantsonly.ui

import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.food2you_restaurantsonly.R
import com.example.food2you_restaurantsonly.databinding.LoginFragmentBinding
import com.example.food2you_restaurantsonly.databinding.RegisterFragmentBinding
import com.example.food2you_restaurantsonly.other.BasicAuthInterceptor
import com.example.food2you_restaurantsonly.other.Constants.KEY_EMAIL
import com.example.food2you_restaurantsonly.other.Constants.KEY_PASSWORD
import com.example.food2you_restaurantsonly.other.RegAndLoginUtility
import com.example.food2you_restaurantsonly.other.RegAndLoginUtility.authenticateApi
import com.example.food2you_restaurantsonly.other.RegAndLoginUtility.isLoggedIn
import com.example.food2you_restaurantsonly.other.Status
import com.example.food2you_restaurantsonly.viewmodels.AuthViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment: Fragment(R.layout.register_fragment) {

    private lateinit var binding: RegisterFragmentBinding
    private val model: AuthViewModel by viewModels()

    @Inject
    lateinit var basicAuthInterceptor: BasicAuthInterceptor

    @Inject
    lateinit var sharedPrefs: SharedPreferences

    private var currentEmail: String? = null
    private var currentPassword: String? = null

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

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        if(isLoggedIn(sharedPrefs, currentEmail ?: "", currentPassword ?: "")) {
            redirectLogin()
        }

        subscribeToObservers()

        binding.clickHereBtn.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }


        binding.regButton.setOnClickListener {
            val email = binding.userNameEt.text.toString()
            val password = binding.passwordEt.text.toString()
            val confirmPassword = binding.confirmPasswordEt.text.toString()

            currentEmail = email
            currentPassword = password

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

                        sharedPrefs.edit()
                            .putString(KEY_EMAIL, currentEmail)
                            .putString(KEY_PASSWORD, currentPassword)
                            .apply()

                        authenticateApi(currentEmail ?: "", currentPassword ?: "", basicAuthInterceptor)

                        redirectLogin()
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


    private fun redirectLogin() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.registerFragment, true)
            .setPopUpTo(R.id.loginFragment, true)
            .build()

        findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToMyRestaurantsFragment(), navOptions)
    }

}