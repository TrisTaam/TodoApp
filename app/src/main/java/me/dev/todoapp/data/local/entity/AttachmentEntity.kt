package me.dev.todoapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import me.dev.todoapp.data.base.model.Attachment

@Entity(
    tableName = "attachment", foreignKeys = [
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = ["id"],
            childColumns = ["task_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AttachmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String?,
    val url: String,
    @ColumnInfo(name = "task_id", index = true) val taskId: Int
) {
    companion object {
        fun from(attachment: Attachment): AttachmentEntity {
            return AttachmentEntity(
                id = attachment.id,
                type = attachment.type,
                url = attachment.url,
                taskId = attachment.taskId
            )
        }
    }

    fun toAttachment(): Attachment {
        return Attachment(
            id = this.id,
            type = this.type,
            url = this.url,
            taskId = this.taskId
        )
    }
}
