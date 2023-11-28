package me.dev.todoapp.utils.recyclerview

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import me.dev.todoapp.data.base.model.Identifiable
import me.dev.todoapp.utils.list.BaseList

abstract class BaseAdapter<T : Identifiable, B : ViewBinding, VH : BaseViewHolder<T, B>> :
    RecyclerView.Adapter<VH>() {
    protected val list: BaseList<T> = BaseList()

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(list[position])
    }

    private fun getDiffResult(newList: List<T>): DiffUtil.DiffResult =
        DiffUtil.calculateDiff(
            BaseDiffUtilCallback<T>(newList, list as List<T>)
        )

    fun setData(newList: BaseList<T>) {
        this.list.clear()
        this.list.addAll(newList)
    }

    fun setData(newList: List<T>) {
        this.list.clear()
        this.list.addAll(newList)
    }

    fun setDataWithDiffUtil(newList: BaseList<T>) {
        val diffResult: DiffUtil.DiffResult = getDiffResult(newList as List<T>)
        setData(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    fun setDataWithDiffUtil(newList: List<T>) {
        val diffResult: DiffUtil.DiffResult = getDiffResult(newList)
        setData(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    class NoDiffResultFound : Exception()
}

