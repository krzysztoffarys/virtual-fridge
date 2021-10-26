package com.example.virtualfridge.data.model.fridge

import com.example.virtualfridge.data.model.fridge.Hit

data class ImageResponse(
    val hits: List<Hit>,
    val total: Int,
    val totalHits: Int
)