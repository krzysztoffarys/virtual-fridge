package com.example.virtualfridge.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.virtualfridge.databinding.RecipeBinding
import com.example.virtualfridge.data.model.recipe.Result

class RecipeAdapter : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {
    inner class RecipeViewHolder(val binding: RecipeBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }
    private val differ = AsyncListDiffer(this, diffCallback)

    var recipes: List<Result>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    private var onItemClickListener: ((Int) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = RecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val curRecipe = recipes[position]
        Glide.with(holder.itemView)
            .load(curRecipe.image)
            .into(holder.binding.ivRecipe)



        holder.binding.tvRecipeTitle.text = curRecipe.title

        holder.binding.ivRecipe.setOnClickListener {
            onItemClickListener?.let { click ->
                click(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun setOnItemClickListener(onItemClick: ((Int) -> Unit)?) {
        this.onItemClickListener = onItemClick
    }

}