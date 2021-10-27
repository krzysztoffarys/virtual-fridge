package com.example.virtualfridge.ui.selectImage

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.virtualfridge.R
import com.example.virtualfridge.adapters.SelectImageAdapter
import com.example.virtualfridge.databinding.ActivityProductImageBinding
import com.example.virtualfridge.databinding.ImageBinding
import com.example.virtualfridge.other.Status
import dagger.hilt.android.AndroidEntryPoint

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
        viewModel.provideImages(10, product.name)

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
                    binding.progressBar.visibility = View.GONE
                    val data = result.data
                    if (data != null) {
                        selectImageAdapter.hits = data.hits
                    }
                }
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    val message = result.message ?: this.getString(R.string.unknown_error)
                    Toast.makeText(this, message.toString(), Toast.LENGTH_SHORT).show()
                }
                Status.LOADING ->{
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        })
    }
}