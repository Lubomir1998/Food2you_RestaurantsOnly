package com.example.food2you_restaurantsonly.other

import android.content.SharedPreferences
import androidx.navigation.NavOptions
import com.example.food2you_restaurantsonly.R
import com.example.food2you_restaurantsonly.other.Constants.KEY_EMAIL
import com.example.food2you_restaurantsonly.other.Constants.KEY_PASSWORD
import com.example.food2you_restaurantsonly.other.Constants.NO_EMAIL
import com.example.food2you_restaurantsonly.other.Constants.NO_PASSWORD

object RegAndLoginUtility {

    fun authenticateApi(email: String, password: String, basicAuthInterceptor: BasicAuthInterceptor) {
        basicAuthInterceptor.email = email
        basicAuthInterceptor.password = password
    }

    fun isLoggedIn(sharedPreferences: SharedPreferences, _email: String, _password: String): Boolean {
        var email = _email
        var password = _password
        email = sharedPreferences.getString(KEY_EMAIL, NO_EMAIL) ?: NO_EMAIL
        password = sharedPreferences.getString(KEY_PASSWORD, NO_PASSWORD) ?: NO_PASSWORD

        return email != NO_EMAIL && password != NO_PASSWORD
    }


}