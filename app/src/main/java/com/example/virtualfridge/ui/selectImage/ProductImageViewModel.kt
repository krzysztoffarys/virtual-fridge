package com.example.virtualfridge.ui.selectImage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.virtualfridge.data.model.ImageResponse
import com.example.virtualfridge.data.model.Product
import com.example.virtualfridge.other.Resource
import com.example.virtualfridge.repositories.FridgeRepository
import com.google.firebase.firestore.CollectionReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProductImageViewModel @Inject constructor(
    private val repository: FridgeRepository,
    private val productCollection: CollectionReference
) : ViewModel() {
    private val _imageResponseStatus = MutableLiveData<Resource<ImageResponse>>()
    val imageResponseStatus: LiveData<Resource<ImageResponse>> = _imageResponseStatus



    fun provideImages(size: Int, search: String) =
        viewModelScope.launch {
            try {
                val response = repository.getImages(searchQuery = search, perPage = size)
                if(response.isSuccessful && response.body() != null) {
                    val body = response.body()
                    _imageResponseStatus.postValue(Resource.success(body))
                } else {
                    _imageResponseStatus.postValue(
                        Resource.error(
                        response.message() ?: "An unknown error occurred", null))
                }
            } catch (e: Exception) {
                _imageResponseStatus.postValue(
                    Resource.error(
                    e.message ?: "An unknown error occurred", null))
            }
        }

    fun updateProductUrl(product: Product, url: String) = viewModelScope.launch {
        productCollection
            .whereEqualTo("name", product.name)
            .whereEqualTo("amount", product.amount)
            .get()
            .addOnCompleteListener { query ->
                if(query.isSuccessful) {
                    val documents = query.result?.documents
                    if (documents!!.isNotEmpty()) {
                        for (document in documents) {
                            try {
                                productCollection.document(document.id).update("url", url)
                            } catch (e: Exception) {
                                Timber.d(e)
                            }
                        }
                    }
                }
            }
    }

}


