package me.dev.todoapp.data.base.repository

import androidx.lifecycle.LiveData
import me.dev.todoapp.data.base.model.Subtask

interface ISubtaskRepository {
    suspend fun insertSubtask(vararg subtask: Subtask)
    suspend fun updateSubtask(subtask: Subtask)
    suspend fun deleteSubtask(subtask: Subtask)
    fun getSubtask(id: Int): LiveData<Subtask>
    fun getAllSubtaskOfTask(taskId: Int): LiveData<List<Subtask>>
}