package me.dev.todoapp.utils.recyclerview

import androidx.recyclerview.widget.DiffUtil
import me.dev.todoapp.data.base.model.Identifiable

class BaseDiffUtilCallback<T : Identifiable>(
    private val newList: List<T>,
    private val oldList: List<T>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}