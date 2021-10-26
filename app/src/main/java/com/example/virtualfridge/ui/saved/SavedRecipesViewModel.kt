package com.example.virtualfridge.ui.saved

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.virtualfridge.data.model.searchRecipe.Result
import com.example.virtualfridge.repositories.FridgeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SavedRecipesViewModel @Inject constructor(
    private val repository: FridgeRepository
) : ViewModel() {

    fun savedRecipes() = repository.getAllRecipes()

}