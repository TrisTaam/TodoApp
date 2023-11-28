package me.dev.todoapp.screen.task.taskdetail

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.dev.todoapp.TodoApplication
import me.dev.todoapp.data.base.model.Attachment
import me.dev.todoapp.data.base.model.Category
import me.dev.todoapp.data.base.model.Subtask
import me.dev.todoapp.data.base.model.Task
import me.dev.todoapp.data.base.repository.IRepositoryBuilder
import me.dev.todoapp.utils.Alarm
import me.dev.todoapp.utils.observeOnce
import java.util.Date

class TaskDetailViewModel(private val repositoryBuilder: IRepositoryBuilder) : ViewModel() {
    private var taskId: Int = -1
    var taskStatus: Boolean = false
    private var categoryId: Int? = null
    lateinit var taskName: String
    var taskDate: Date? = null
    var taskRepeat: String? = null
    var taskNote: String? = null
    val subtasks: LiveData<List<Subtask>>
        get() = repositoryBuilder.subtaskRepository.getAllSubtaskOfTask(taskId)

    private val _category: MutableLiveData<Category?> = MutableLiveData()

    val category: LiveData<Category?>
        get() = _category
    lateinit var attachments: List<Attachment>

    fun setupWithTask(id: Int, lifecycleOwner: LifecycleOwner): LiveData<Task> {
        taskId = id
        return repositoryBuilder.taskRepository.getTask(taskId).apply {
            observeOnce(lifecycleOwner) {
                taskStatus = it.status
                taskName = it.name
                categoryId = it.categoryId
                observeCategoryLiveData(lifecycleOwner)
                taskDate = it.dueDate
                taskRepeat = it.repeat
                taskNote = it.notes
            }
        }
    }

    private fun observeCategoryLiveData(lifecycleOwner: LifecycleOwner) {
        categoryId?.let { id ->
            repositoryBuilder.categoryRepository.getCategory(id).observeOnce(lifecycleOwner) {
                _category.value = it
            }
        }
    }

    fun saveChange(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            cancelAlarm(context, taskId, taskName, "You have a task to do")
            repositoryBuilder.taskRepository.updateTask(
                Task(
                    id = taskId,
                    status = taskStatus,
                    name = taskName,
                    dueDate = taskDate,
                    categoryId = categoryId,
                    repeat = taskRepeat,
                    notes = taskNote
                )
            )
            createAlarm(
                context,
                taskId,
                taskName,
                "You have a task to do",
                taskDate!!
            )
        }
    }

    private fun createAlarm(context: Context, id: Int, title: String, message: String, date: Date) {
        val bundle = Bundle()
        bundle.putString("title", title)
        bundle.putString("message", message)
        Alarm.createAlarm(context, bundle, id, date)
    }

    private fun cancelAlarm(context: Context, id: Int, title: String, message: String) {
        val bundle = Bundle()
        bundle.putString("title", title)
        bundle.putString("message", message)
        Alarm.cancelAlarm(context, bundle, id)
    }

    fun newSubtask(defaultName: String = "New Subtask") {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryBuilder.subtaskRepository.insertSubtask(
                Subtask(
                    taskId = taskId,
                    name = defaultName
                )
            )
        }
    }

    fun updateSubtask(subtask: Subtask) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryBuilder.subtaskRepository.updateSubtask(subtask)
        }
    }

    fun deleteSubtask(subtask: Subtask) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryBuilder.subtaskRepository.deleteSubtask(subtask)
        }
    }

    fun setCategory(category: Category?) {
        _category.value = category
        categoryId = category?.id
    }

    fun deleteThisTask() {
        if (taskId == -1) return
        viewModelScope.launch(Dispatchers.IO) {
            repositoryBuilder.taskRepository.deleteTask(taskId)
        }
    }

    fun getAllCategory(): LiveData<List<Category>> =
        repositoryBuilder.categoryRepository.getAllCategory()

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val application = this.application as TodoApplication
            return TaskDetailViewModel(application.repositoryBuilder) as T
        }
    }
}