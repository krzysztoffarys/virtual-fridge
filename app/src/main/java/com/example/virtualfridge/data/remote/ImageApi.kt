package com.example.virtualfridge.data.remote

import com.example.virtualfridge.data.model.ImageResponse
import com.example.virtualfridge.other.Key.PIXABAY_API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageApi {
    @GET("/api")
    suspend fun getImages(
        @Query("key")
        key: String = PIXABAY_API_KEY,
        @Query("q")
        searchQuery: String,
        @Query("per_page")
        perPage: Int = 6,
        @Query("page")
        page: Int = 1
    ) : Response<ImageResponse>
}