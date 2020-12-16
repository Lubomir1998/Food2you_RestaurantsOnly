package com.example.food2you_restaurantsonly.di

import com.example.food2you_restaurantsonly.other.BasicAuthInterceptor
import com.example.food2you_restaurantsonly.data.RestApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {


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


}