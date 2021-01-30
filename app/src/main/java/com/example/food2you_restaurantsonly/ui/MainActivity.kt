package com.example.food2you_restaurantsonly.ui

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.food2you_restaurantsonly.NavGraphDirections
import com.example.food2you_restaurantsonly.R
import com.example.food2you_restaurantsonly.Repository
import com.example.food2you_restaurantsonly.data.remote.PushNotification
import com.example.food2you_restaurantsonly.data.remote.UserToken
import com.example.food2you_restaurantsonly.databinding.ActivityMainBinding
import com.example.food2you_restaurantsonly.other.BasicAuthInterceptor
import com.example.food2you_restaurantsonly.other.Constants
import com.example.food2you_restaurantsonly.other.Constants.KEY_EMAIL
import com.example.food2you_restaurantsonly.other.Constants.KEY_NAME
import com.example.food2you_restaurantsonly.other.Constants.KEY_PASSWORD
import com.example.food2you_restaurantsonly.other.Constants.KEY_RES_ID
import com.example.food2you_restaurantsonly.other.Constants.KEY_TOKEN
import com.example.food2you_restaurantsonly.other.Constants.NOTIFICATION_TAP
import com.example.food2you_restaurantsonly.other.Constants.NO_EMAIL
import com.example.food2you_restaurantsonly.other.Constants.NO_PASSWORD
import com.example.food2you_restaurantsonly.viewmodels.AddRestaurantViewModel
import com.google.firebase.iid.FirebaseInstanceId
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityMainBinding
    private val model: AddRestaurantViewModel by viewModels()

    @Inject lateinit var sharedPrefs: SharedPreferences
    @Inject lateinit var repository: Repository
    @Inject lateinit var basicAuthInterceptor: BasicAuthInterceptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.home -> {
                    Navigation.findNavController(this, R.id.navHostFragment).navigate(NavGraphDirections.actionLaunchStartingFragment())
                }
                R.id.myRes -> {
                    Navigation.findNavController(this, R.id.navHostFragment).navigate(NavGraphDirections.actionLaunchAddRestaurantFragment(sharedPrefs.getString(KEY_RES_ID, "") ?: ""))
                }
                R.id.previews -> {
                    Navigation.findNavController(this, R.id.navHostFragment).navigate(NavGraphDirections.actionLaunchPreviewFragment())
                }
                R.id.waitingOrders -> {
                    Navigation.findNavController(this, R.id.navHostFragment).navigate(NavGraphDirections.actionLaunchWaitingOrdersFragment(sharedPrefs.getString(KEY_NAME, "") ?: ""))
                }
                R.id.onTheWayOrders -> {

                }
                R.id.signOut -> {
                    logOut()
                }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        if(intent.action == NOTIFICATION_TAP) {
            navigateWhenNotificationIsTapped()
        }

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            sharedPrefs.edit().putString(KEY_TOKEN, it.token).apply()

            val email = sharedPrefs.getString(KEY_EMAIL, "") ?: ""
            val userToken = UserToken(it.token)
            sharedPrefs.edit().putString(KEY_TOKEN, it.token).apply()

            if(email.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        repository.registerOwnerToken(userToken, email)
                        repository.changeRestaurantToken(userToken, email)
                    } catch (e: Exception) { }
                }
            }
        }



    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        intent?.let {
            if(it.action == NOTIFICATION_TAP) {
                navigateWhenNotificationIsTapped()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if(intent.action == NOTIFICATION_TAP) {
            navigateWhenNotificationIsTapped()
        }
    }


    private fun navigateWhenNotificationIsTapped() {
        val email = sharedPrefs.getString(KEY_EMAIL, NO_EMAIL) ?: NO_EMAIL
        val password = sharedPrefs.getString(KEY_PASSWORD, NO_PASSWORD) ?: NO_PASSWORD

        if(email != NO_EMAIL && password != NO_PASSWORD) {
            basicAuthInterceptor.email = email
            basicAuthInterceptor.password = password
        }

        val action = NavGraphDirections.actionLaunchWaitingOrdersFragment(sharedPrefs.getString(KEY_NAME, "") ?: "")
        Navigation.findNavController(this, R.id.navHostFragment).navigate(action)
    }

    private fun logOut() {

        model.deleteFood()
        model.deleteRestaurant()

        sharedPrefs.edit()
                .putString(KEY_EMAIL, NO_EMAIL)
                .putString(KEY_PASSWORD, NO_PASSWORD)
                .putString(KEY_NAME, "")
                .apply()

        val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.myRestaurantsFragment, true)
                .build()

        Navigation.findNavController(this, R.id.navHostFragment).navigate(NavGraphDirections.actionLaunchLoginFragment(), navOptions)
    }
}