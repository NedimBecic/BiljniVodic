package com.example.spirala1.DAO

import com.example.spirala1.Models.TrefleSearchResponse
import com.example.spirala1.Models.TrefleSingleSearch
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TrefleApi {

    @GET("plants/search")
    suspend fun searchBiljke(
        @Query("q") query: String,
        @Query("token") token: String
    ): Response<TrefleSearchResponse>

    @GET("plants/{plantId}")
    suspend fun getBiljkeDetails(
        @Path("plantId") plantId: String,
        @Query("token") token: String
    ): Response<TrefleSingleSearch>

    @GET("plants/search")
    suspend fun getBiljkeWithFlowerColor(
        @Query("filter[flower_color]") flowerColor: String,
        @Query("q") query: String,
        @Query("token") token: String,
        @Query("page") page: Int
    ): Response<TrefleSearchResponse>
}
