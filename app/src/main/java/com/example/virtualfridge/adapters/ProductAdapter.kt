package com.example.virtualfridge.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.virtualfridge.data.model.Product
import com.example.virtualfridge.databinding.ProductBinding
import com.example.virtualfridge.ui.dialogs.SelectDialogListener
import com.example.virtualfridge.ui.dialogs.SelectImageDialog
import com.example.virtualfridge.ui.fridge.FridgeViewModel
import timber.log.Timber

class ProductAdapter(
    val viewModel: FridgeViewModel,
    val context: Context
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    inner class ProductViewHolder(val binding: ProductBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }
    private val differ = AsyncListDiffer(this, diffCallback)

    var products: List<Product>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val curProduct = products[position]
        holder.binding.apply {
            tvName.text = curProduct.name
            tvAmount.text = curProduct.amount.toString()

            ivAdd.setOnClickListener {
                viewModel.updateProductAmount(
                    curProduct, Product(curProduct.name, curProduct.amount + 1)
                )
            }

            ivDelete.setOnClickListener {
                viewModel.deleteProduct(curProduct)
            }

            ivRemove.setOnClickListener {
                viewModel.updateProductAmount(
                    curProduct, Product(curProduct.name, curProduct.amount - 1)
                )
            }
            ivItem.setOnClickListener { imageView ->
                SelectImageDialog(context,
                    listOf(
                        "https://picsum.photos/100",
                        "https://picsum.photos/200",
                        "https://picsum.photos/300",
                        "https://picsum.photos/400",
                        "https://picsum.photos/500",
                        "https://picsum.photos/600",
                    ),
                    object : SelectDialogListener {
                        override fun onAddButtonClicked(selectedUrl: String) {

                        }
                    }
                ).show()
            }

        }
    }

    override fun getItemCount(): Int {
        return products.size
    }
}