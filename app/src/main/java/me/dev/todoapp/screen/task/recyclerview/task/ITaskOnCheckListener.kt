package me.dev.todoapp.screen.task.recyclerview.task

import me.dev.todoapp.data.base.model.Task

interface ITaskOnCheckListener {
    fun onCheck(status: Boolean, task: Task)
}