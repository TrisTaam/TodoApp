package me.dev.todoapp.data.base.repository

interface IRepositoryBuilder {
    val taskRepository: ITaskRepository
    val subtaskRepository: ISubtaskRepository
    val categoryRepository: ICategoryRepository
    val attachmentRepository: IAttachmentRepository
}