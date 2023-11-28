package me.dev.todoapp.data.base.model


data class Subtask(
    override val id: Int = 0,
    val taskId: Int,
    val name: String,
    var status: Boolean = false
) : Identifiable
