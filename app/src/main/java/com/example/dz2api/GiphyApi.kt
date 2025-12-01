package com.example.dz2api

import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyApi {

    @GET("v1/gifs/random")
    suspend fun getRandomGif(
        @Query("api_key") apiKey: String,
        @Query("tag") tag: String = "",
        @Query("rating") rating: String = "g"
    ): RandomGifResponse
}
