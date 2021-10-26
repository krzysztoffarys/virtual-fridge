package com.example.virtualfridge.ui.dialogs

import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import com.example.virtualfridge.data.model.fridge.Product
import com.example.virtualfridge.databinding.DialogAddProductBinding

class AddProductDialog(context: Context, var addDialogListener: AddDialogListener) :
    AppCompatDialog(context) {
    private lateinit var binding: DialogAddProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogAddProductBinding.inflate(layoutInflater)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)

        binding.tvAdd.setOnClickListener {
            val name = binding.etName.text.toString()
            val amount = binding.etAmount.text.toString()
            if(name.isEmpty()) {
                Toast.makeText(context, "Please enter a name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(amount.isEmpty()) {
                Toast.makeText(context, "Please enter amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val item = Product(name, amount.toInt())
            addDialogListener.onAddButtonClicked(item)
            dismiss()
        }

        binding.tvCancel.setOnClickListener {
            cancel()
        }

    }

}