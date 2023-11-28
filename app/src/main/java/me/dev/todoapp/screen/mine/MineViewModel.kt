package me.dev.todoapp.screen.mine

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.data.PieEntry
import me.dev.todoapp.TodoApplication
import me.dev.todoapp.data.base.model.CategoryCount
import me.dev.todoapp.data.base.model.Task
import me.dev.todoapp.data.base.repository.IRepositoryBuilder
import java.util.Calendar
import java.util.Date

class MineViewModel(private val repositoryBuilder: IRepositoryBuilder) : ViewModel() {

    private val _completedTasksCount: MutableLiveData<Int> = MutableLiveData(0)
    val completedTasksCount: LiveData<Int> = _completedTasksCount
    private val _pendingTasksCount: MutableLiveData<Int> = MutableLiveData(0)
    val pendingTasksCount: LiveData<Int> = _pendingTasksCount
    private val _tasksInNext7Days: MutableLiveData<List<Task>> = MutableLiveData()
    val tasksInNext7Days: LiveData<List<Task>> = _tasksInNext7Days
    private val _taskPieEntries: MutableLiveData<List<PieEntry>> = MutableLiveData()
    val taskPieEntries: LiveData<List<PieEntry>> = _taskPieEntries

    fun initObserver(lifecycleOwner: LifecycleOwner) {
        observeTaskCounter(lifecycleOwner)
        observeTaskInNext7Days(lifecycleOwner)
        observeTaskStats(lifecycleOwner)
    }

    private fun observeTaskStats(lifecycleOwner: LifecycleOwner) {
        repositoryBuilder.taskRepository.getCategoryCounts().observe(lifecycleOwner) { list ->
            val entries: MutableList<PieEntry> = mutableListOf()
            val sortedList: MutableList<CategoryCount> =
                list.sortedByDescending { it.taskCount }.toMutableList()
            for (i in 0..4) {
                if (i == sortedList.size) {
                    _taskPieEntries.value = entries
                    return@observe
                } else {
                    entries.add(
                        PieEntry(
                            sortedList[i].taskCount.toFloat(),
                            sortedList[i].categoryName
                        )
                    )
                }
            }
            var otherCount = 0
            for (i in 5..<sortedList.size) {
                otherCount += sortedList[i].taskCount
            }
            entries.add(PieEntry(otherCount.toFloat(), "Others"))
            _taskPieEntries.value = entries
        }
    }

    private fun observeTaskInNext7Days(lifecycleOwner: LifecycleOwner) {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startTime = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val endTime = calendar.timeInMillis
        repositoryBuilder.taskRepository.getAllTaskInTimeRange(startTime, endTime)
            .observe(lifecycleOwner) {
                _tasksInNext7Days.value = it
            }
    }

    private fun observeTaskCounter(lifecycleOwner: LifecycleOwner) {
        repositoryBuilder.taskRepository.getTaskCountByStatus(true).observe(lifecycleOwner) {
            _completedTasksCount.value = it
        }
        repositoryBuilder.taskRepository.getTaskCountByStatus(false).observe(lifecycleOwner) {
            _pendingTasksCount.value = it
        }
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val application = this.application as TodoApplication
            return MineViewModel(application.repositoryBuilder) as T
        }
    }
}