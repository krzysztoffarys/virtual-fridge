package com.example.virtualfridge.repositories

import com.example.virtualfridge.data.local.RecipeDao
import com.example.virtualfridge.data.model.searchRecipe.Result
import com.example.virtualfridge.data.remote.ImageApi
import com.example.virtualfridge.data.remote.RecipeApi

class FridgeRepository(
    private val imageApi: ImageApi,
    private val recipeApi: RecipeApi,
    private val recipeDao: RecipeDao
) {
    suspend fun getImages(searchQuery: String, perPage: Int = 6) = imageApi.getImages(searchQuery = searchQuery, perPage = perPage)

    suspend fun getRecipes(searchQuery: String) = recipeApi.getRecipes(searchQuery = searchQuery)

    suspend fun getDetailRecipe(id: String) = recipeApi.getRecipeById(id = id)

    fun getAllRecipes() = recipeDao.getAllRecipes()

    suspend fun delete(recipe: Result) = recipeDao.deleteRecipe(recipe)

    suspend fun insert(recipe: Result) = recipeDao.insertRecipe(recipe)
}