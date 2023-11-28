package me.dev.todoapp.screen.task.dialog.addtask

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.dev.todoapp.TodoApplication
import me.dev.todoapp.data.base.model.Category
import me.dev.todoapp.data.base.model.Subtask
import me.dev.todoapp.data.base.model.Task
import me.dev.todoapp.data.base.repository.IRepositoryBuilder
import me.dev.todoapp.utils.Alarm
import me.dev.todoapp.utils.CallbackResult
import me.dev.todoapp.utils.list.BaseList
import java.util.Date

class AddTaskDialogViewModel(private val repositoryBuilder: IRepositoryBuilder) : ViewModel() {

    var categoryId: Int? = null
    var date: Date? = null
    var repeat: String? = null
    var result: Int = CallbackResult.FAILED

    private val subtasks: BaseList<Subtask> = BaseList()

    fun setSubtaskListOnChangeListener(listener: (newList: BaseList<Subtask>) -> Unit) {
        subtasks.setOnChangeListener { listener.invoke(it) }
    }

    fun newSubtask(defaultName: String = "New Subtask"): Int {
        subtasks.apply {
            setNewItemInitializer { id ->
                Subtask(id = id, name = "$defaultName $id", taskId = 0)
            }
            return insertNewItem()
        }
    }

    fun deleteSubtask(subtask: Subtask): Int = subtasks.deleteAndGetIndex(subtask)

    fun updateSubtask(subtask: Subtask): Int = subtasks.updateAndGetIndex(subtask)

    fun getAllCategory(): LiveData<List<Category>> =
        repositoryBuilder.categoryRepository.getAllCategory()

    fun insertTaskWithSubtasks(context: Context, taskName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val id = repositoryBuilder.taskRepository.insertTask(
                Task(
                    name = taskName,
                    categoryId = categoryId,
                    dueDate = date,
                    repeat = repeat
                )
            )
            createAlarm(
                context,
                id,
                taskName,
                "You have a task to do",
                date!!
            )
            val subtasksArray = subtasks.map { it.copy(id = 0, taskId = id) }.toTypedArray()
            repositoryBuilder.subtaskRepository.insertSubtask(*subtasksArray)
            result = CallbackResult.SUCCESS
        }
    }

    private fun createAlarm(context: Context, id: Int, title: String, message: String, date: Date) {
        val bundle = Bundle()
        bundle.putString("title", title)
        bundle.putString("message", message)
        Alarm.createAlarm(context, bundle, id, date)
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val application = this.application as TodoApplication
            return AddTaskDialogViewModel(application.repositoryBuilder) as T
        }
    }
}