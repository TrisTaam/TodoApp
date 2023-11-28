package me.dev.todoapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import me.dev.todoapp.data.local.entity.AttachmentEntity

@Dao
interface AttachmentDao {
    @Insert(entity = AttachmentEntity::class)
    suspend fun insert(attachmentEntity: AttachmentEntity)

    @Update(entity = AttachmentEntity::class)
    suspend fun update(attachmentEntity: AttachmentEntity)

    @Delete(entity = AttachmentEntity::class)
    suspend fun delete(attachmentEntity: AttachmentEntity)

    @Query("SELECT * FROM attachment WHERE id = :id")
    fun getAttachment(id: Int): LiveData<AttachmentEntity>
}