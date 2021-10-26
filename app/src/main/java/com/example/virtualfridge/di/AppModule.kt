package com.example.virtualfridge.di

import com.example.virtualfridge.data.remote.ImageApi
import com.example.virtualfridge.data.remote.RecipeApi
import com.example.virtualfridge.other.Constants.PIXABAY_BASE_URL
import com.example.virtualfridge.other.Constants.SPOONACULAR_BASE_URL
import com.example.virtualfridge.repositories.FridgeRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    //firebase
    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFireStore() = Firebase.firestore

    @Provides
    fun provideProductCollection(fireStore: FirebaseFirestore, auth: FirebaseAuth) = fireStore.collection("${auth.uid}")

    //retrofit
    @Provides
    fun provideOkHttpClient() = run {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }
    @Singleton
    @Provides
    @Named("RetrofitInstanceImages")
    fun provideRetrofitImages(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(PIXABAY_BASE_URL)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    @Named("RetrofitInstanceRecipes")
    fun provideRetrofitRecipes(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(SPOONACULAR_BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideApiImages(
        @Named("RetrofitInstanceImages") retrofit: Retrofit) = retrofit.create(ImageApi::class.java)

    @Provides
    @Singleton
    fun provideApiRecipes(
        @Named("RetrofitInstanceRecipes") retrofit: Retrofit) = retrofit.create(RecipeApi::class.java)

    @Singleton
    @Provides
    fun provideRepository(imageApi: ImageApi, recipeApi: RecipeApi) = FridgeRepository(imageApi, recipeApi)

}