package com.example.virtualfridge.ui.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.virtualfridge.R
import com.example.virtualfridge.data.model.detailRecipe.DetailRecipeResponse
import com.example.virtualfridge.data.model.searchRecipe.Result
import com.example.virtualfridge.other.Resource
import com.example.virtualfridge.repositories.FridgeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: FridgeRepository
) : ViewModel() {

    private val _detailRecipeResponseStatus = MutableLiveData<Resource<DetailRecipeResponse>>()
    val detailRecipeResponseStatus: LiveData<Resource<DetailRecipeResponse>> = _detailRecipeResponseStatus

    private val _isSaved = MutableLiveData<Boolean>()
    val isSaved: LiveData<Boolean> = _isSaved


    fun getRecipeDetail(id: Int) =
        viewModelScope.launch {
            _detailRecipeResponseStatus.postValue(Resource.loading(null))
            try {
                val response = repository.getDetailRecipe(id.toString())
                if(response.isSuccessful && response.body() != null) {
                    val body = response.body()
                    _detailRecipeResponseStatus.postValue(Resource.success(body))
                } else {
                    _detailRecipeResponseStatus.postValue(
                        Resource.error(
                            response.message() ?: "An unknown error occurred", null))
                }
            } catch (e: Exception) {
                _detailRecipeResponseStatus.postValue(
                    Resource.error(
                        e.message ?: "An unknown error occurred", null))
            }
        }

    fun saveRecipe(recipe: Result) = viewModelScope.launch {
        try {
            repository.insert(recipe)
        } catch (e: Exception) {
            Timber.d(e)
        }
    }

    fun deleteRecipe(recipe: Result) = viewModelScope.launch {
        try {
            repository.delete(recipe)
        } catch (e: Exception) {
            Timber.d(e)
        }
    }


    fun checkIfRecipeIsSaved(recipe: Result) = viewModelScope.launch {
        try {
            _isSaved.postValue(repository.isRecipeInDatabase(recipe))
        } catch (e: Exception) {
            Timber.d(e)
        }
    }
}