package me.dev.todoapp.data.local.repository

import android.content.Context
import me.dev.todoapp.data.base.repository.IAttachmentRepository
import me.dev.todoapp.data.base.repository.ICategoryRepository
import me.dev.todoapp.data.base.repository.IRepositoryBuilder
import me.dev.todoapp.data.base.repository.ISubtaskRepository
import me.dev.todoapp.data.base.repository.ITaskRepository
import me.dev.todoapp.data.local.LocalDatabase

class LocalRepositoryBuilder(private val context: Context) : IRepositoryBuilder {
    override val taskRepository: ITaskRepository
        get() = LocalTaskRepository(LocalDatabase.getInstance(context).taskDao())
    override val subtaskRepository: ISubtaskRepository
        get() = LocalSubtaskRepository(LocalDatabase.getInstance(context).subtaskDao())
    override val categoryRepository: ICategoryRepository
        get() = LocalCategoryRepository(LocalDatabase.getInstance(context).categoryDao())
    override val attachmentRepository: IAttachmentRepository
        get() = LocalAttachmentRepository(LocalDatabase.getInstance(context).attachmentDao())
}