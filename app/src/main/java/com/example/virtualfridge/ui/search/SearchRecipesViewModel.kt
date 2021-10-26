package com.example.virtualfridge.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.virtualfridge.data.model.searchRecipe.RecipeResponse
import com.example.virtualfridge.other.Resource
import com.example.virtualfridge.repositories.FridgeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchRecipesViewModel @Inject constructor(
    private val repository: FridgeRepository
): ViewModel() {

    private val _recipeResponseStatus = MutableLiveData<Resource<RecipeResponse>>()
    val recipeResponseStatus: LiveData<Resource<RecipeResponse>> = _recipeResponseStatus

    fun provideRecipes(search: String) =
        viewModelScope.launch {
            _recipeResponseStatus.postValue(Resource.loading(null))
            try {
                val response = repository.getRecipes(search)
                if(response.isSuccessful && response.body() != null) {
                    val body = response.body()
                    _recipeResponseStatus.postValue(Resource.success(body))
                } else {
                    _recipeResponseStatus.postValue(
                        Resource.error(
                            response.message() ?: "An unknown error occurred", null))
                }
            } catch (e: Exception) {
                _recipeResponseStatus.postValue(
                    Resource.error(
                        e.message ?: "An unknown error occurred", null))
            }
        }
}