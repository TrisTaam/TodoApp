package me.dev.todoapp.screen.task.recyclerview.task

import android.view.LayoutInflater
import android.view.ViewGroup
import me.dev.todoapp.data.base.model.Task
import me.dev.todoapp.databinding.ViewholderTaskBinding
import me.dev.todoapp.utils.recyclerview.BaseAdapter

class TaskAdapter : BaseAdapter<Task, ViewholderTaskBinding, TaskViewHolder>() {

    private var onCheckListener: ITaskOnCheckListener? = null
    private var onClickListener: ITaskOnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding: ViewholderTaskBinding = ViewholderTaskBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TaskViewHolder(binding, onCheckListener, onClickListener)
    }

    fun setOnCheckListener(onCheckListener: ITaskOnCheckListener) {
        this.onCheckListener = onCheckListener
    }

    fun setOnClickListener(onClickListener: ITaskOnClickListener) {
        this.onClickListener = onClickListener
    }

}