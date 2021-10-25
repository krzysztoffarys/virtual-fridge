package com.example.virtualfridge.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.virtualfridge.data.model.Product
import com.example.virtualfridge.databinding.ProductBinding
import com.example.virtualfridge.ui.fridge.FridgeViewModel

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
    private var onItemClickListener: ((Product) -> Unit)? = null

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
            if(curProduct.url != null) {
                loadImage(curProduct.url!!, ivItem)
            }

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


            ivItem.setOnClickListener {
                onItemClickListener?.let { click ->
                    click(curProduct)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }

    private fun loadImage(url: String, imageView: ImageView) = Glide.with(imageView)
        .load(url)
        .into(imageView)

    fun setOnItemClickListener(onItemClick: ((Product) -> Unit)?) {
        this.onItemClickListener = onItemClick
    }
}