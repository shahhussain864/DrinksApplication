package com.realestate.testapplication.repository

import com.realestate.testapplication.Room.MyAppDatabase
import com.realestate.testapplication.model.ProductResponse
import com.realestate.testapplication.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import javax.inject.Inject

class Repository @Inject constructor(var apiService: ApiService
) {

    fun getData(s: String): Flow<ProductResponse?> = flow {
        val response = apiService.getProducts(s)
        emit(response)
    }



}