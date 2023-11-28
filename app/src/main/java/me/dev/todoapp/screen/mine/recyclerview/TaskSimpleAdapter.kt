package me.dev.todoapp.screen.mine.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import me.dev.todoapp.data.base.model.Task
import me.dev.todoapp.databinding.ViewholderTaskSimpleBinding
import me.dev.todoapp.utils.recyclerview.BaseAdapter

class TaskSimpleAdapter : BaseAdapter<Task, ViewholderTaskSimpleBinding, TaskSimpleViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskSimpleViewHolder {
        val binding = ViewholderTaskSimpleBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TaskSimpleViewHolder(binding)
    }
}