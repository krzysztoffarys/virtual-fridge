package com.example.virtualfridge.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.virtualfridge.data.model.searchRecipe.Result

@Database(
    entities = [Result::class],
    version = 1
)
abstract class RecipeDatabase : RoomDatabase() {

    abstract fun getRecipeDao(): RecipeDao
}