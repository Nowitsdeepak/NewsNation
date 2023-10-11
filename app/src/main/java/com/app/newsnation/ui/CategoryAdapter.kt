package com.app.newsnation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.app.newsnation.R
import com.app.newsnation.databinding.ItemCategoriesBinding
import com.app.newsnation.model.Category

class CategoryAdapter(private val clist: List<Category>, val onItemClicked: ((String) -> Unit)) :
    Adapter<CategoryAdapter.CategoryViewHolder>() {
    var selectedPosition = 0

    inner class CategoryViewHolder(var binding: ItemCategoriesBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val layout =
            ItemCategoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CategoryViewHolder(layout)
    }

    override fun getItemCount(): Int = clist.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = clist[position]

        with(holder) {
            with(binding) {

                tvCategory.text = category.categoryName


                itemView.setOnClickListener {

                    selectedPosition = adapterPosition

                    onItemClicked(category.categoryName)

                }
            }
        }
    }
}