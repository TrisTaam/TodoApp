package me.dev.todoapp.screen.task.recyclerview.task

import me.dev.todoapp.data.base.model.Task

interface ITaskOnClickListener {
    fun onClick(task: Task)
}