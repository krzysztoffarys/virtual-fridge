package com.example.virtualfridge.ui.dialogs

import com.example.virtualfridge.data.model.Product

interface AddDialogListener {
    fun onAddButtonClicked(product: Product)
}