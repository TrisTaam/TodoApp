package me.dev.todoapp.data.local.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import me.dev.todoapp.data.base.model.CategoryCount
import me.dev.todoapp.data.base.model.Task
import me.dev.todoapp.data.base.repository.ITaskRepository
import me.dev.todoapp.data.local.dao.TaskDao
import me.dev.todoapp.data.local.entity.TaskEntity

class LocalTaskRepository(private val dao: TaskDao) : ITaskRepository {
    override suspend fun insertTask(task: Task): Int {
        return dao.insert(TaskEntity.from(task)).toInt()
    }

    override suspend fun updateTask(task: Task) {
        dao.update(TaskEntity.from(task))
    }

    override suspend fun deleteTask(task: Task) {
        dao.delete(TaskEntity.from(task))
    }

    override suspend fun deleteTask(id: Int) {
        dao.delete(id)
    }

    override fun getTask(id: Int): LiveData<Task> {
        return dao.getTaskEntity(id).map { it.toTask() }
    }

    override fun getAllTask(): LiveData<List<Task>> {
        return dao.getAllTaskEntity().map { list ->
            list.map { it.toTask() }
        }
    }

    override fun getAllTaskOfCategory(categoryId: Int): LiveData<List<Task>> {
        return dao.getAllTaskOfCategory(categoryId).map { list ->
            list.map { it.toTask() }
        }
    }

    override fun getAllTaskContainsTitle(keyword: String): LiveData<List<Task>> {
        return dao.getAllTaskContainsTitle(keyword).map { list ->
            list.map { it.toTask() }
        }
    }

    override fun getAllTaskInTimeRange(startTime: Long, endTime: Long): LiveData<List<Task>> {
        return dao.getAllTaskInTimeRange(startTime, endTime).map { list ->
            list.map { it.toTask() }
        }
    }

    override fun getTaskCountByStatus(status: Boolean): LiveData<Int> {
        return dao.getTaskCountByStatus(status)
    }

    override fun getCategoryCounts(): LiveData<List<CategoryCount>> {
        return dao.getCategoryCounts()
    }

}