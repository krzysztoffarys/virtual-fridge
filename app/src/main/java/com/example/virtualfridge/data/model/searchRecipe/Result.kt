package com.example.virtualfridge.data.model.searchRecipe

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Result(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val image: String = "",
    val imageType: String = "jpg",
    val title: String = ""
)