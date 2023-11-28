package me.dev.todoapp.screen.calendar

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.dev.todoapp.TodoApplication
import me.dev.todoapp.data.base.model.Task
import me.dev.todoapp.data.base.repository.IRepositoryBuilder
import me.dev.todoapp.utils.Formatter
import me.dev.todoapp.utils.observeOnce
import java.util.Date

class CalendarViewModel(private val repositoryBuilder: IRepositoryBuilder) : ViewModel() {

    private val _tasks: MutableLiveData<List<Task>> = MutableLiveData()
    val tasks: LiveData<List<Task>> get() = _tasks

    fun setDate(date: Date, lifecycleOwner: LifecycleOwner) {
        val dateString: String = Formatter.dateToString(date, Formatter.DateFormat.DATE)
        repositoryBuilder.taskRepository.getAllTask().observeOnce(lifecycleOwner) { list ->
            _tasks.value = list
                .filter {
                    if (it.dueDate != null) {
                        val taskDate: String =
                            Formatter.dateToString(it.dueDate, Formatter.DateFormat.DATE)
                        taskDate == dateString
                    } else false
                }
                .sortedWith { p0, p1 ->
                    p0.dueDate?.compareTo(p1.dueDate) ?: 0
                }
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repositoryBuilder.taskRepository.updateTask(task)
        }
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val application = this.application as TodoApplication
            return CalendarViewModel(application.repositoryBuilder) as T
        }
    }
}