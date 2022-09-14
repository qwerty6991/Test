package com.example.test.data.retrofit

import retrofit2.http.GET
import retrofit2.http.Query

interface GifsApi {

    @GET("search")
    suspend fun getGifs(
        @Query("api_key") key: String,
        @Query("q") searchBy: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("rating") rating: String,
        @Query("lang") lang: String
    ): GifsResponse
}