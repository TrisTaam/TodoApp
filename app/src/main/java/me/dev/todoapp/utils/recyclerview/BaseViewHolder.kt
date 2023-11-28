package me.dev.todoapp.utils.recyclerview

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<T, B : ViewBinding>(
    protected open val binding: B,
) : RecyclerView.ViewHolder(binding.root) {

    abstract fun bind(item: T)
}