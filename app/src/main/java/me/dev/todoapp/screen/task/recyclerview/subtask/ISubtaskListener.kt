package me.dev.todoapp.screen.task.recyclerview.subtask

import me.dev.todoapp.data.base.model.Subtask

interface ISubtaskListener {
    fun onCheck(subtask: Subtask)

    fun removeButtonOnClick(subtask: Subtask)

    fun afterEditName(subtask: Subtask)
}