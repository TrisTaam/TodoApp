package me.dev.todoapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import me.dev.todoapp.data.base.model.CategoryCount
import me.dev.todoapp.data.local.entity.TaskEntity

@Dao
interface TaskDao {
    @Insert(entity = TaskEntity::class)
    suspend fun insert(taskEntity: TaskEntity): Long

    @Update(entity = TaskEntity::class)
    suspend fun update(taskEntity: TaskEntity)

    @Delete(entity = TaskEntity::class)
    suspend fun delete(taskEntity: TaskEntity)

    @Query("DELETE FROM task WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM task WHERE id = :id")
    fun getTaskEntity(id: Int): LiveData<TaskEntity>

    @Query("SELECT * FROM task")
    fun getAllTaskEntity(): LiveData<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE category_id = :categoryId")
    fun getAllTaskOfCategory(categoryId: Int): LiveData<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE name LIKE '%' || :keyword || '%'")
    fun getAllTaskContainsTitle(keyword: String): LiveData<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE due_date BETWEEN :startTime AND :endTime")
    fun getAllTaskInTimeRange(startTime: Long, endTime: Long): LiveData<List<TaskEntity>>

    @Query("SELECT COUNT(*) FROM task WHERE status = :status")
    fun getTaskCountByStatus(status: Boolean): LiveData<Int>

    @Query("SELECT category.name AS categoryName, COUNT(task.id) AS taskCount FROM category LEFT JOIN task ON category.id = task.category_id GROUP BY categoryName")
    fun getCategoryCounts(): LiveData<List<CategoryCount>>
}