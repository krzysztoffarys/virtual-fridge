package com.example.virtualfridge.data.model.fridge

import java.io.Serializable

data class Product (
    val name: String = "",
    val amount: Int = 0,
    var url: String? = null
) : Serializable
