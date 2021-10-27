package com.example.virtualfridge.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.virtualfridge.R
import com.example.virtualfridge.databinding.ActivityFridgeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FridgeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFridgeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFridgeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = findNavController(R.id.navHostFragment)
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}