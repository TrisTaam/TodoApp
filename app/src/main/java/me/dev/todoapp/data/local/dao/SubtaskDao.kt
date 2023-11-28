package me.dev.todoapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import me.dev.todoapp.data.local.entity.SubtaskEntity

@Dao
interface SubtaskDao {
    @Insert(entity = SubtaskEntity::class)
    suspend fun insert(vararg subtaskEntity: SubtaskEntity)

    @Update(entity = SubtaskEntity::class)
    suspend fun update(subtaskEntity: SubtaskEntity)

    @Delete(entity = SubtaskEntity::class)
    suspend fun delete(subtaskEntity: SubtaskEntity)

    @Query("SELECT * FROM subtask WHERE id = :id")
    fun getSubtaskEntity(id: Int): LiveData<SubtaskEntity>

    @Query("SELECT * FROM subtask WHERE task_id = :taskId")
    fun getAllSubtaskOfTask(taskId: Int): LiveData<List<SubtaskEntity>>
}