package com.example.virtualfridge.data.model.recipe

data class RecipeResponse(
    val number: Int,
    val offset: Int,
    val results: List<Result>,
    val totalResults: Int
)