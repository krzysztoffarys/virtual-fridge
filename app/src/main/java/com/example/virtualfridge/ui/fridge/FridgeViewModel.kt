package com.example.virtualfridge.ui.fridge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.virtualfridge.data.model.fridge.Product
import com.example.virtualfridge.other.Resource
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FridgeViewModel @Inject constructor(
    private val productCollection: CollectionReference,
) : ViewModel() {

    private val _productsStatus = MutableLiveData<Resource<List<Product>>>()
    val productsStatus: LiveData<Resource<List<Product>>> = _productsStatus

    fun insertProduct(product: Product) = viewModelScope.launch {
        productCollection
            .whereEqualTo("name", product.name)
            .get()
            .addOnCompleteListener { query ->
                if(query.isSuccessful) {
                    //if product with this name exists, then we add the amount
                    val documents = query.result?.documents
                    if (documents!!.isNotEmpty()) {
                        for (document in documents) {
                            try {
                                val oldProduct = document.toObject<Product>()
                                productCollection.document(document.id).update("amount", product.amount + (oldProduct?.amount
                                    ?: 0))
                            } catch (e: Exception) {
                                Timber.d(e)
                            }
                        }
                    } else {
                        productCollection.add(product)
                    }
                }
            }
    }

    fun deleteProduct(product: Product) = viewModelScope.launch {
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
                                productCollection.document(document.id).delete()
                            } catch (e: Exception) {
                                Timber.d(e)
                            }
                        }
                    }
                }
            }
    }

    fun updateProductAmount(oldProduct: Product, newProduct: Product) = viewModelScope.launch {
        if (newProduct.amount <= 0) {
            return@launch
        }
        productCollection
            .whereEqualTo("name", oldProduct.name)
            .whereEqualTo("amount", oldProduct.amount)
            .get()
            .addOnCompleteListener { query ->
                if(query.isSuccessful) {
                    val documents = query.result?.documents
                    if (documents!!.isNotEmpty()) {
                        for (document in documents) {
                            try {
                                productCollection.document(document.id).update("amount", newProduct.amount)
                            } catch (e: Exception) {
                                Timber.d(e)
                            }
                        }
                    }
                }
            }
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