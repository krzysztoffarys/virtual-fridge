package com.example.virtualfridge.ui.selectImage

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.virtualfridge.adapters.SelectImageAdapter
import com.example.virtualfridge.databinding.ActivityProductImageBinding
import com.example.virtualfridge.databinding.ImageBinding
import com.example.virtualfridge.other.Status
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ProductImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductImageBinding
    private lateinit var selectImageAdapter: SelectImageAdapter
    val viewModel: ProductImageViewModel by viewModels()
    private val args: ProductImageActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val product = args.product

        binding = ActivityProductImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        subscribeToObservers()
        viewModel.provideImages(12, product.name)

        selectImageAdapter.setOnItemClickListener { position ->
            var holder = binding.rv.findViewHolderForAdapterPosition(selectImageAdapter.selectedItem)
            var bindingProduct = holder?.let { ImageBinding.bind(it.itemView) }
            bindingProduct?.iv?.alpha = 0.3F
            selectImageAdapter.selectedItem = position
            holder = binding.rv.findViewHolderForAdapterPosition(selectImageAdapter.selectedItem)
            bindingProduct = holder?.let { ImageBinding.bind(it.itemView) }
            bindingProduct?.iv?.alpha = 1F
        }

        binding.btnSelect.setOnClickListener {
            viewModel.updateProductUrl(product, selectImageAdapter.hits[selectImageAdapter.selectedItem].largeImageURL)
            finish()
        }
        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() = binding.rv.apply {
        selectImageAdapter = SelectImageAdapter()
        adapter = selectImageAdapter
        layoutManager = GridLayoutManager(this@ProductImageActivity, 2)

    }

    private fun subscribeToObservers() {

        viewModel.imageResponseStatus.observe(this, { result ->
            when(result.status) {
                Status.SUCCESS -> {
                    val data = result.data
                    Timber.d("Halo")
                    if (data != null) {
                        selectImageAdapter.hits = data.hits
                        Timber.d("Halo ${data.hits.size}")
                    }
                }
                Status.ERROR -> {
                    val message = result.message ?: "An unknown error occurred"
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING ->{

                }
            }
        })
    }
}