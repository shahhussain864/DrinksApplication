package com.realestate.testapplication.dagger

import android.content.Context
import androidx.room.Room
import com.realestate.testapplication.BaseClass
import com.realestate.testapplication.Room.MyAppDatabase
import com.realestate.testapplication.utils.Constants
import com.realestate.testapplication.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Moduldes {

    @Provides
    fun getbaseUrl():String = Constants.BaseUrl



    @Provides
    @Singleton
    fun getRetrofit(baseurl:String):Retrofit{
        return Retrofit.Builder()
            .baseUrl(baseurl).
            addConverterFactory(GsonConverterFactory.create()).
            build()
            }

    @Provides
    @Singleton
    fun getService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }


}