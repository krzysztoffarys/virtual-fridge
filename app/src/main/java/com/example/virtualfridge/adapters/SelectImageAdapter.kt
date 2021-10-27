package com.example.virtualfridge.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.virtualfridge.data.model.fridge.Hit
import com.example.virtualfridge.databinding.ImageBinding

class SelectImageAdapter : RecyclerView.Adapter<SelectImageAdapter.ImageViewHolder>() {
    inner class ImageViewHolder(val binding: ImageBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Hit>() {
        override fun areItemsTheSame(oldItem: Hit, newItem: Hit): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Hit, newItem: Hit): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }
    private val differ = AsyncListDiffer(this, diffCallback)

    var hits: List<Hit>
        get() = differ.currentList
        set(value) = differ.submitList(value)


    private var onItemClickListener: ((Int) -> Unit)? = null
    var selectedItem = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        val curImage = hits[position]
        if (position != selectedItem) {
            holder.binding.iv.alpha = 0.3F
        }


        Glide.with(holder.binding.root)
            .load(curImage.largeImageURL)
            .into(holder.binding.iv)

        holder.binding.iv.setOnClickListener {
            onItemClickListener?.let { click ->
                click(position)
            }
        }


    }

    override fun getItemCount(): Int {
        return hits.size
    }

    fun setOnItemClickListener(onItemClick: ((Int) -> Unit)?) {
        this.onItemClickListener = onItemClick
    }

}