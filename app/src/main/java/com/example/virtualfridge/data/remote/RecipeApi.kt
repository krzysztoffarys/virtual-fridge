package com.example.virtualfridge.data.remote

import com.example.virtualfridge.data.model.detailRecipe.DetailRecipeResponse
import com.example.virtualfridge.data.model.searchRecipe.RecipeResponse
import com.example.virtualfridge.other.Key.SPOONACULAR_API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApi {
    @GET("/recipes/complexSearch")
    suspend fun getRecipes(
        @Query("apiKey")
        key: String = SPOONACULAR_API_KEY,
        @Query("query")
        searchQuery: String,
    ) : Response<RecipeResponse>

    @GET("/recipes/{id}/information")
    suspend fun getRecipeById(
        @Path("id") id: String,
        @Query("apiKey")
        key: String = SPOONACULAR_API_KEY
    ) : Response<DetailRecipeResponse>
}