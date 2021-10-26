package com.example.virtualfridge.repositories

import com.example.virtualfridge.data.remote.ImageApi
import com.example.virtualfridge.data.remote.RecipeApi

class FridgeRepository(
    private val imageApi: ImageApi,
    private val recipeApi: RecipeApi
) {
    suspend fun getImages(searchQuery: String, perPage: Int = 6) = imageApi.getImages(searchQuery = searchQuery, perPage = perPage)

    suspend fun getRecipes(searchQuery: String) = recipeApi.getRecipes(searchQuery = searchQuery)
}