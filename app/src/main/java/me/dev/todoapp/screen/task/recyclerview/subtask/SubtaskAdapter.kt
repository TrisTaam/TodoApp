package me.dev.todoapp.screen.task.recyclerview.subtask

import android.view.LayoutInflater
import android.view.ViewGroup
import me.dev.todoapp.data.base.model.Subtask
import me.dev.todoapp.databinding.ViewholderSubtaskBinding
import me.dev.todoapp.utils.recyclerview.BaseAdapter

class SubtaskAdapter(private val listener: ISubtaskListener) :
    BaseAdapter<Subtask, ViewholderSubtaskBinding, SubtaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubtaskViewHolder {
        val binding: ViewholderSubtaskBinding = ViewholderSubtaskBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SubtaskViewHolder(binding, listener)
    }
}