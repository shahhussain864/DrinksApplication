package com.realestate.testapplication.network

import com.realestate.testapplication.model.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("json/v1/1/search.php")
    suspend fun getProducts(@Query("s") s: String): ProductResponse?
}