package com.example.food2you_restaurantsonly.ui

import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.food2you_restaurantsonly.R
import com.example.food2you_restaurantsonly.databinding.LoginFragmentBinding
import com.example.food2you_restaurantsonly.other.BasicAuthInterceptor
import com.example.food2you_restaurantsonly.other.Constants
import com.example.food2you_restaurantsonly.other.RegAndLoginUtility
import com.example.food2you_restaurantsonly.other.RegAndLoginUtility.authenticateApi
import com.example.food2you_restaurantsonly.other.RegAndLoginUtility.isLoggedIn
import com.example.food2you_restaurantsonly.other.Status
import com.example.food2you_restaurantsonly.viewmodels.AuthViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment: Fragment(R.layout.login_fragment) {

    private lateinit var binding: LoginFragmentBinding
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
        binding = LoginFragmentBinding.inflate(inflater, container, false)
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
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.loginButton.setOnClickListener {
            val email = binding.userNameEt.text.toString()
            val password = binding.passwordEt.text.toString()

            currentEmail = email
            currentPassword = password

            model.loginRestaurant(email, password)
        }

    }



    private fun redirectLogin() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.registerFragment, true)
            .setPopUpTo(R.id.loginFragment, true)
            .build()

        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMyRestaurantsFragment(), navOptions)
    }

    private fun subscribeToObservers() {
        model.loginStatus.observe(viewLifecycleOwner, {
            it?.let { result ->
                when(result.status) {

                    Status.SUCCESS -> {
                        binding.progressBar2.visibility = View.GONE

                        Snackbar.make(requireView(), "Successfully logged in", Snackbar.LENGTH_LONG).show()

                        sharedPrefs.edit()
                            .putString(Constants.KEY_EMAIL, currentEmail)
                            .putString(Constants.KEY_PASSWORD, currentPassword)
                            .apply()

                        authenticateApi(currentEmail ?: "", currentPassword ?: "", basicAuthInterceptor)
                        redirectLogin()
                    }

                    Status.ERROR -> {
                        binding.progressBar2.visibility = View.GONE
                        Snackbar.make(requireView(), result.message ?: "An unknown error occured", Snackbar.LENGTH_LONG).show()
                    }

                    Status.LOADING -> {
                        binding.progressBar2.visibility = View.VISIBLE
                    }

                }

            }
        })
    }

}










