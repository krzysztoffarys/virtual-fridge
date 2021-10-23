package com.example.virtualfridge.ui.fridge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.virtualfridge.data.model.Product
import com.example.virtualfridge.other.Resource
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FridgeViewModel @Inject constructor(
    private val productCollection: CollectionReference
) : ViewModel() {

    private val _productsStatus = MutableLiveData<Resource<List<Product>>>()
    val productsStatus: LiveData<Resource<List<Product>>> = _productsStatus

    fun insertProduct(product: Product) = viewModelScope.launch {
        productCollection.add(product)
    }

    fun subscribeToRealtimeUpdates() {
        _productsStatus.postValue(Resource.loading(null))
        productCollection.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            firebaseFirestoreException?.let {
                _productsStatus.postValue(Resource.error(it.toString(), null))
                return@addSnapshotListener
            }
            querySnapshot?.let {
                val list = mutableListOf<Product>()
                for(document in it) {
                    val product = document.toObject<Product>()
                    list.add(product)
                }
                _productsStatus.postValue(Resource.success(list))
            }
        }
    }
}