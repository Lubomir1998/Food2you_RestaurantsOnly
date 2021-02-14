package com.example.food2you_restaurantsonly.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.PowerManager
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.food2you_restaurantsonly.R
import com.example.food2you_restaurantsonly.Repository
import com.example.food2you_restaurantsonly.data.remote.UserToken
import com.example.food2you_restaurantsonly.other.Constants.CHANNEL_ID
import com.example.food2you_restaurantsonly.other.Constants.KEY_EMAIL
import com.example.food2you_restaurantsonly.other.Constants.KEY_TOKEN
import com.example.food2you_restaurantsonly.other.Constants.NOTIFICATION_TAP
import com.example.food2you_restaurantsonly.other.Constants.NO_EMAIL
import com.example.food2you_restaurantsonly.ui.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class FirebaseService: FirebaseMessagingService() {

    @Inject
    lateinit var repository: Repository
    @Inject
    lateinit var sharedPrefs: SharedPreferences


    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)

        val email = sharedPrefs.getString(KEY_EMAIL, NO_EMAIL) ?: NO_EMAIL

        val userToken = UserToken(newToken)
        sharedPrefs.edit().putString(KEY_TOKEN, newToken).apply()

        if(email.isNotEmpty() && email != NO_EMAIL) {
            CoroutineScope(Dispatchers.IO).launch {
                repository.registerOwnerToken(userToken, email)
                repository.changeRestaurantToken(userToken, email)
            }
        }

    }


    @SuppressLint("InvalidWakeLockTag")
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val email = sharedPrefs.getString(KEY_EMAIL, "") ?: ""

        if(email == NO_EMAIL || email.isEmpty()) return

        // wake the screen after receiving the notification
        val pm = applicationContext.getSystemService(Context.POWER_SERVICE) as PowerManager
        val isScreenOn = pm.isScreenOn
        if (!isScreenOn) {
            val wl = pm.newWakeLock(
                    PowerManager.FULL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.ON_AFTER_RELEASE,
                    "MyLock"
            )
            wl.acquire(10000)
            val wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyCpuLock")
            wl_cpu.acquire(10000)
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            action = NOTIFICATION_TAP
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random().nextInt()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(message.data["title"])
                .setContentText(message.data["message"])
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()

        notificationManager.notify(notificationID, notification)


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channelName = "channelName"
        val channel = NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
            description = "My channel description"
            enableLights(true)
            lightColor = Color.GREEN
        }
        notificationManager.createNotificationChannel(channel)
    }



}