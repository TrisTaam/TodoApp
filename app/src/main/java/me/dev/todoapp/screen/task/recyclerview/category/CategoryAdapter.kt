package me.dev.todoapp.screen.task.recyclerview.category

import android.view.LayoutInflater
import android.view.ViewGroup
import me.dev.todoapp.data.base.model.Category
import me.dev.todoapp.databinding.ViewholderCategoryBinding
import me.dev.todoapp.utils.recyclerview.BaseAdapter

class CategoryAdapter(private val listener: ICategoryListener) :
    BaseAdapter<Category, ViewholderCategoryBinding, CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ViewholderCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CategoryViewHolder(binding, listener)
    }

}