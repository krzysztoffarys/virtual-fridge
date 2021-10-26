package com.example.virtualfridge.data.model.detailRecipe

data class WinePairing(
    val pairedWines: List<String>,
    val pairingText: String,
    val productMatches: List<ProductMatche>
)