package com.example.virtualfridge.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.virtualfridge.data.model.searchRecipe.Result

@Dao
interface  RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: Result)

    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): LiveData<List<Result>>

    @Delete
    suspend fun deleteRecipe(recipe: Result)

}