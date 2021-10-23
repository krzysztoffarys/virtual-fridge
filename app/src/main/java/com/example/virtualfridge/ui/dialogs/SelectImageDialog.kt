package com.example.virtualfridge.ui.dialogs

import android.content.Context
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.virtualfridge.databinding.DialogSelectImageBinding
import timber.log.Timber


class SelectImageDialog(context: Context, private val images: List<String>,
private val selectDialogListener: SelectDialogListener) :
    AppCompatDialog(context) {
    private lateinit var binding: DialogSelectImageBinding
    private var selectedImage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("init")
        super.onCreate(savedInstanceState)
        binding = DialogSelectImageBinding.inflate(layoutInflater)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        setupImages()

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnSelect.setOnClickListener {
            selectDialogListener.onAddButtonClicked(images[selectedImage])
            dismiss()
        }

    }

    private fun setupImages() {
        val imageViews = listOf(
            binding.iv0,
            binding.iv1,
            binding.iv2,
            binding.iv3,
            binding.iv4,
            binding.iv5
        )

        for (i in imageViews.indices) {
            if (i != selectedImage) {
                imageViews[i].alpha = 0.3F
            }
            val url = images[i]
            Glide.with(context)
                .load(url)
                .centerCrop()
                .into(imageViews[i])

            imageViews[i].setOnClickListener {
                imageViews[selectedImage].alpha = 0.3F
                imageViews[i].alpha = 1F
                selectedImage = i
            }
        }

    }
}