package me.dev.todoapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.dev.todoapp.data.local.dao.AttachmentDao
import me.dev.todoapp.data.local.dao.CategoryDao
import me.dev.todoapp.data.local.dao.SubtaskDao
import me.dev.todoapp.data.local.dao.TaskDao
import me.dev.todoapp.data.local.entity.AttachmentEntity
import me.dev.todoapp.data.local.entity.CategoryEntity
import me.dev.todoapp.data.local.entity.SubtaskEntity
import me.dev.todoapp.data.local.entity.TaskEntity

@Database(
    entities = [
        TaskEntity::class,
        SubtaskEntity::class,
        CategoryEntity::class,
        AttachmentEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun subtaskDao(): SubtaskDao
    abstract fun categoryDao(): CategoryDao
    abstract fun attachmentDao(): AttachmentDao

    companion object {
        @Volatile
        private var instance: LocalDatabase? = null

        fun getInstance(context: Context): LocalDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(context, LocalDatabase::class.java, "todo_local_database")
                    .build()
                    .also {
                        instance = it
                    }
            }
        }
    }
}