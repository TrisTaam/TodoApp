package me.dev.todoapp.data.local.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import me.dev.todoapp.data.base.model.Subtask
import me.dev.todoapp.data.base.repository.ISubtaskRepository
import me.dev.todoapp.data.local.dao.SubtaskDao
import me.dev.todoapp.data.local.entity.SubtaskEntity

class LocalSubtaskRepository(private val dao: SubtaskDao) : ISubtaskRepository {
    override suspend fun insertSubtask(vararg subtask: Subtask) {
        val subtaskEntities: Array<out SubtaskEntity> =
            subtask.map { SubtaskEntity.from(it) }.toTypedArray()
        dao.insert(*subtaskEntities)
    }

    override suspend fun updateSubtask(subtask: Subtask) {
        dao.update(SubtaskEntity.from(subtask))
    }

    override suspend fun deleteSubtask(subtask: Subtask) {
        dao.delete(SubtaskEntity.from(subtask))
    }

    override fun getSubtask(id: Int): LiveData<Subtask> {
        return dao.getSubtaskEntity(id).map { it.toSubtask() }
    }

    override fun getAllSubtaskOfTask(taskId: Int): LiveData<List<Subtask>> {
        return dao.getAllSubtaskOfTask(taskId).map { list ->
            list.map { it.toSubtask() }
        }
    }

}