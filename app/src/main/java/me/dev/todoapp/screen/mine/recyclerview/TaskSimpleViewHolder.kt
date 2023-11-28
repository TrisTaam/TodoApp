package me.dev.todoapp.screen.mine.recyclerview

import me.dev.todoapp.data.base.model.Task
import me.dev.todoapp.databinding.ViewholderTaskSimpleBinding
import me.dev.todoapp.utils.Formatter
import me.dev.todoapp.utils.recyclerview.BaseViewHolder

class TaskSimpleViewHolder(binding: ViewholderTaskSimpleBinding) :
    BaseViewHolder<Task, ViewholderTaskSimpleBinding>(binding) {

    override fun bind(item: Task) {
        binding.taskSimpleName.text = item.name
        if (item.dueDate != null) {
            val datetime: String =
                Formatter.dateToString(item.dueDate, Formatter.DateFormat.SHORTEN_DATE)
            binding.taskSimpleDatetime.text = datetime
        }
    }
}