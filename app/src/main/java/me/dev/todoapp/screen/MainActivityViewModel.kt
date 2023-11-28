package me.dev.todoapp.screen

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.dev.todoapp.TodoApplication
import me.dev.todoapp.data.base.model.Category
import me.dev.todoapp.data.base.model.Task
import me.dev.todoapp.data.base.repository.IRepositoryBuilder
import me.dev.todoapp.utils.observeOnce

class MainActivityViewModel(private val repositoryBuilder: IRepositoryBuilder) : ViewModel() {
    enum class TaskSort {
        A_Z, Z_A, DATE_TIME, CREATION_TIME
    }

    private var _tasks: MutableLiveData<List<Task>> = MutableLiveData()
    val tasks: LiveData<List<Task>> get() = _tasks

    private val _categoryId: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>(0)
    }
    val categoryId: LiveData<Int> get() = _categoryId

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repositoryBuilder.taskRepository.updateTask(task)
        }
    }

    private var getAllTask: () -> LiveData<List<Task>> =
        { repositoryBuilder.taskRepository.getAllTask() }

    private fun updateAllTask(lifecycleOwner: LifecycleOwner) {
        getAllTask.invoke().observeOnce(lifecycleOwner) {
            _tasks.value = it
        }
    }

    fun setCategoryFilter(id: Int, lifecycleOwner: LifecycleOwner) {
        _categoryId.value = id
        getAllTask =
            if (_categoryId.value == 0) {
                { repositoryBuilder.taskRepository.getAllTask() }
            } else {
                { repositoryBuilder.taskRepository.getAllTaskOfCategory(id) }
            }
        updateAllTask(lifecycleOwner)
    }

    fun setTaskFilter(keyword: String, lifecycleOwner: LifecycleOwner) {
        val key: String = keyword.trim()
        getAllTask =
            if (key == "") {
                { repositoryBuilder.taskRepository.getAllTask() }
            } else {
                { repositoryBuilder.taskRepository.getAllTaskContainsTitle(key) }
            }
        updateAllTask(lifecycleOwner)
    }

    fun sortCurrentList(sortType: TaskSort, lifecycleOwner: LifecycleOwner) {
        getAllTask.invoke().observeOnce(lifecycleOwner) {
            when (sortType) {
                TaskSort.A_Z -> {
                    _tasks.value = it.sortedWith { p0, p1 ->
                        p0.name.compareTo(p1.name)
                    }
                }

                TaskSort.Z_A -> {
                    _tasks.value = it.sortedWith { p0, p1 ->
                        p1.name.compareTo(p0.name)
                    }
                }

                TaskSort.DATE_TIME -> {
                    _tasks.value = it.sortedWith { p0, p1 ->
                        if (p0.dueDate != null && p1.dueDate != null) {
                            p0.dueDate.compareTo(p1.dueDate)
                        } else {
                            if (p0.dueDate == null) 1
                            else 0
                        }
                    }
                }

                TaskSort.CREATION_TIME -> {
                    _tasks.value = it
                }
            }
        }
    }

    fun getAllCategory(): LiveData<List<Category>> =
        repositoryBuilder.categoryRepository.getAllCategory()

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val application = this.application as TodoApplication
            return MainActivityViewModel(application.repositoryBuilder) as T
        }
    }
}