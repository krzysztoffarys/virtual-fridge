package com.example.virtualfridge.data.model

data class ImageResponse(
    val hits: List<Hit>,
    val total: Int,
    val totalHits: Int
)