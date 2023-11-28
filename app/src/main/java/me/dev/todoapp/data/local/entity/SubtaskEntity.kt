package me.dev.todoapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import me.dev.todoapp.data.base.model.Subtask

@Entity(
    tableName = "subtask", foreignKeys = [
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = ["id"],
            childColumns = ["task_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SubtaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "task_id", index = true) val taskId: Int,
    val name: String,
    var status: Boolean = false
) {
    companion object {
        fun from(subtask: Subtask): SubtaskEntity {
            return SubtaskEntity(
                id = subtask.id,
                taskId = subtask.taskId,
                name = subtask.name,
                status = subtask.status
            )
        }
    }

    fun toSubtask(): Subtask {
        return Subtask(
            id = this.id,
            taskId = this.taskId,
            name = this.name,
            status = this.status
        )
    }
}
