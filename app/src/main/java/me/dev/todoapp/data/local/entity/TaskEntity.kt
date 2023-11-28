package me.dev.todoapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import me.dev.todoapp.data.base.model.Task
import java.util.Date

@Entity(
    tableName = "task", foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var status: Boolean = false,
    val name: String,
    @ColumnInfo(name = "category_id", index = true) val categoryId: Int?,
    @ColumnInfo(name = "due_date") val dueDate: Date? = null,
    @ColumnInfo(name = "reminder_at") val reminderAt: Int? = null,
    val repeat: String? = null,
    val notes: String? = null,
) {
    companion object {
        fun from(task: Task): TaskEntity {
            return TaskEntity(
                id = task.id,
                status = task.status,
                name = task.name,
                categoryId = task.categoryId,
                dueDate = task.dueDate,
                reminderAt = task.reminderAt,
                repeat = task.repeat,
                notes = task.notes
            )
        }
    }

    fun toTask(): Task {
        return Task(
            id = this.id,
            status = this.status,
            name = this.name,
            categoryId = this.categoryId,
            dueDate = this.dueDate,
            reminderAt = this.reminderAt,
            repeat = this.repeat,
            notes = this.notes
        )
    }
}
