package com.example.virtualfridge.di

import com.example.virtualfridge.other.Constants.PRODUCT_COLLECTION
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFireStore() = Firebase.firestore

    @Provides
    @Singleton
    fun provideProductCollection(fireStore: FirebaseFirestore) = fireStore.collection(PRODUCT_COLLECTION)

}