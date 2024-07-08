package com.example.spirala1.Adapters

import com.example.spirala1.DAO.TrefleApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://trefle.io/api/v1/"

    val api: TrefleApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TrefleApi::class.java)
    }
}