package com.example.virtualfridge.repositories

import com.example.virtualfridge.data.local.RecipeDao
import com.example.virtualfridge.data.model.fridge.Product
import com.example.virtualfridge.data.model.searchRecipe.Result
import com.example.virtualfridge.data.remote.ImageApi
import com.example.virtualfridge.data.remote.RecipeApi
import com.example.virtualfridge.other.Resource
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class FridgeRepository(
    private val imageApi: ImageApi,
    private val recipeApi: RecipeApi,
    private val recipeDao: RecipeDao,
    private val productCollection: CollectionReference
) {
    suspend fun getImages(searchQuery: String, perPage: Int = 6) = imageApi.getImages(searchQuery = searchQuery, perPage = perPage)

    suspend fun getRecipes(searchQuery: String) = recipeApi.getRecipes(searchQuery = searchQuery)

    suspend fun getDetailRecipe(id: String) = recipeApi.getRecipeById(id = id)

    fun getAllRecipes() = recipeDao.getAllRecipes()

    suspend fun delete(recipe: Result) = recipeDao.deleteRecipe(recipe)

    suspend fun insert(recipe: Result) = recipeDao.insertRecipe(recipe)

    suspend fun isRecipeInDatabase(recipe: Result) : Boolean {
        return recipeDao.getRecipeById(recipe.id) != null
    }

    //firebase
    fun insertProduct(product: Product) {
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

    fun deleteProduct(product: Product) {
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

    fun updateProductAmount(oldProduct: Product, newProduct: Product) {
        if (newProduct.amount <= 0) {
            return
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

}