package me.dev.todoapp.screen.task.dialog.addtask

interface IAddTaskListener {
    fun onFinish(result: Int, categoryId: Int?)
}