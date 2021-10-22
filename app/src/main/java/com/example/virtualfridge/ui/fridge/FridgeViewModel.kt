package com.example.virtualfridge.ui.fridge

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.CollectionReference
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FridgeViewModel @Inject constructor(
    productCollection: CollectionReference
) : ViewModel() {
}