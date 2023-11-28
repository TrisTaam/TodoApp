package me.dev.todoapp.screen.task.recyclerview.task

import android.view.View
import me.dev.todoapp.data.base.model.Task
import me.dev.todoapp.databinding.ViewholderTaskBinding
import me.dev.todoapp.utils.Formatter
import me.dev.todoapp.utils.recyclerview.BaseViewHolder

class TaskViewHolder(
    override val binding: ViewholderTaskBinding,
    private val onCheckListener: ITaskOnCheckListener?,
    private val onClickListener: ITaskOnClickListener?
) : BaseViewHolder<Task, ViewholderTaskBinding>(binding) {

    override fun bind(item: Task) {
        // Bind View
        binding.taskNameTxt.text = item.name
        binding.taskCheckbox.isChecked = item.status
        if (item.dueDate != null) {
            binding.taskDateTxt.text =
                Formatter.dateToString(item.dueDate, Formatter.DateFormat.SHORTEN_DATE)
            binding.taskTimeTxt.text =
                Formatter.dateToString(item.dueDate, Formatter.DateFormat.TIME)
            binding.taskDateTxt.visibility = View.VISIBLE
            binding.taskTimeTxt.visibility = View.VISIBLE
        } else {
            binding.taskDateTxt.visibility = View.GONE
            binding.taskTimeTxt.visibility = View.GONE
        }
        binding.taskAlarmIcon.visibility = if (item.reminderAt != null) View.VISIBLE else View.GONE
        binding.taskRepeatIcon.visibility = if (item.repeat != null) View.VISIBLE else View.GONE
        binding.taskNoteIcon.visibility = if (item.notes != null) View.VISIBLE else View.GONE
        // Bind Listener
        binding.taskCheckbox.setOnClickListener {
            onCheckListener?.onCheck(binding.taskCheckbox.isChecked, item)
        }
        binding.root.setOnClickListener {
            onClickListener?.onClick(item)
        }
    }
}