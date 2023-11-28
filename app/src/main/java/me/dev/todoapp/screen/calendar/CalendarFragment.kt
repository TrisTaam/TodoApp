package me.dev.todoapp.screen.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import me.dev.todoapp.data.base.model.Task
import me.dev.todoapp.databinding.FragmentCalendarBinding
import me.dev.todoapp.screen.task.recyclerview.task.ITaskOnCheckListener
import me.dev.todoapp.screen.task.recyclerview.task.TaskAdapter
import java.util.Calendar
import java.util.Date

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<CalendarViewModel> {
        CalendarViewModel.Factory(requireActivity().application)
    }

    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initTaskRcv()
        initCalendar()
    }

    private fun initTaskRcv() {
        taskAdapter = TaskAdapter()
        taskAdapter.setOnCheckListener(object : ITaskOnCheckListener {
            override fun onCheck(status: Boolean, task: Task) {
                viewModel.updateTask(task.also { it.status = status })
            }
        })
        binding.calendarTaskRcv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = taskAdapter
        }
        viewModel.tasks.observe(viewLifecycleOwner) {
            taskAdapter.setDataWithDiffUtil(it)
        }
    }

    private fun initCalendar() {
        binding.calendarView.date = Calendar.getInstance().time.time
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val date: Date = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }.time
            viewModel.setDate(date, viewLifecycleOwner)
        }
        val date = Date(binding.calendarView.date)
        viewModel.setDate(date, viewLifecycleOwner)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}