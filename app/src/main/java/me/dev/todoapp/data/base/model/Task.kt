package me.dev.todoapp.data.base.model

import java.util.Date

data class Task(
    override val id: Int = 0,
    var status: Boolean = false,
    val name: String,
    val categoryId: Int?,
    val dueDate: Date? = null,
    val reminderAt: Int? = null,
    val repeat: String? = null,
    val notes: String? = null,
) : Identifiable {
    companion object {
        const val REPEAT_DAILY = "Daily"
        const val REPEAT_WEEKLY = "Weekly"
        const val REPEAT_MONTHLY = "Monthly"
        const val REPEAT_YEARLY = "Yearly"
    }
}