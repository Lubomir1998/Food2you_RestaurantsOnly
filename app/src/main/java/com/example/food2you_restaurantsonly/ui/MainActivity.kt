package com.example.food2you_restaurantsonly.ui

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import com.example.food2you_restaurantsonly.NavGraphDirections
import com.example.food2you_restaurantsonly.R
import com.example.food2you_restaurantsonly.Repository
import com.example.food2you_restaurantsonly.data.remote.PushNotification
import com.example.food2you_restaurantsonly.data.remote.UserToken
import com.example.food2you_restaurantsonly.other.BasicAuthInterceptor
import com.example.food2you_restaurantsonly.other.Constants
import com.example.food2you_restaurantsonly.other.Constants.KEY_EMAIL
import com.example.food2you_restaurantsonly.other.Constants.KEY_NAME
import com.example.food2you_restaurantsonly.other.Constants.KEY_PASSWORD
import com.example.food2you_restaurantsonly.other.Constants.KEY_TOKEN
import com.example.food2you_restaurantsonly.other.Constants.NOTIFICATION_TAP
import com.example.food2you_restaurantsonly.other.Constants.NO_EMAIL
import com.example.food2you_restaurantsonly.other.Constants.NO_PASSWORD
import com.google.firebase.iid.FirebaseInstanceId
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var sharedPrefs: SharedPreferences
    @Inject lateinit var repository: Repository
    @Inject lateinit var basicAuthInterceptor: BasicAuthInterceptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                    repository.registerOwnerToken(userToken, email)
                    repository.changeRestaurantToken(userToken, email)
                }
            }
        }



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
}