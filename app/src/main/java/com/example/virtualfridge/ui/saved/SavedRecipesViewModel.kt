package com.example.virtualfridge.ui.saved

import androidx.lifecycle.ViewModel
import com.example.virtualfridge.repositories.FridgeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SavedRecipesViewModel @Inject constructor(
    private val repository: FridgeRepository
) : ViewModel() {

    fun savedRecipes() = repository.getAllRecipes()

}