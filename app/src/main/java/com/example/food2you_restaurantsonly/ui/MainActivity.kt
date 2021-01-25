package com.example.food2you_restaurantsonly.ui

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.food2you_restaurantsonly.R
import com.example.food2you_restaurantsonly.data.remote.PushNotification
import com.example.food2you_restaurantsonly.other.Constants
import com.example.food2you_restaurantsonly.other.Constants.KEY_EMAIL
import com.example.food2you_restaurantsonly.other.Constants.KEY_TOKEN
import com.google.firebase.iid.FirebaseInstanceId
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
//            val email = sharedPrefs.getString(KEY_EMAIL, "") ?: ""
//            val pushNotification = PushNotification("On the way!", "Your order is currently on the way", it.token)
//            sharedPrefs.edit().putString(KEY_TOKEN, it.token).apply()
//
//        }



    }
}