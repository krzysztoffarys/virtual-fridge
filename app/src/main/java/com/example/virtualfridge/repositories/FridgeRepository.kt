package com.example.virtualfridge.repositories

import com.example.virtualfridge.data.remote.ImageApi

class FridgeRepository(
    private val imageApi: ImageApi
) {
    suspend fun getImages(searchQuery: String, perPage: Int = 6) = imageApi.getImages(searchQuery = searchQuery, perPage = perPage)
}