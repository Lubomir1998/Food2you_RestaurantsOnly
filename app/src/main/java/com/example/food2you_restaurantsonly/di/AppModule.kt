package com.example.food2you_restaurantsonly.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.food2you_restaurantsonly.data.local.DbHelper
import com.example.food2you_restaurantsonly.other.BasicAuthInterceptor
import com.example.food2you_restaurantsonly.data.remote.RestApi
import com.example.food2you_restaurantsonly.other.Constants.ENCRYPTED_SHARED_PREFS_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideDb(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        DbHelper::class.java,
        "my_db"
    )
        .fallbackToDestructiveMigration()
        .build()


    @Singleton
    @Provides
    fun provideDao(db: DbHelper) = db.getDao()


    @Singleton
    @Provides
    fun provideBasicAuthInterceptor() = BasicAuthInterceptor()


    @Singleton
    @Provides
    fun provideApi(basicAuthInterceptor: BasicAuthInterceptor): RestApi {
        val client = OkHttpClient.Builder()
            .addInterceptor(basicAuthInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl("http://192.168.0.102:8008")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(RestApi::class.java)
    }


    @Singleton
    @Provides
    fun provideEncryptedSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            ENCRYPTED_SHARED_PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

}