package me.dev.todoapp.screen.task.dialog.addtask

import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import me.dev.todoapp.R
import me.dev.todoapp.data.base.model.Subtask
import me.dev.todoapp.databinding.DialogAddTaskBinding
import me.dev.todoapp.screen.task.dialog.datetimepicker.DatetimePickerDialogFragment
import me.dev.todoapp.screen.task.dialog.datetimepicker.IDatetimePickerListener
import me.dev.todoapp.screen.task.recyclerview.subtask.ISubtaskListener
import me.dev.todoapp.screen.task.recyclerview.subtask.SubtaskAdapter
import java.util.Date

class AddTaskDialogFragment : BottomSheetDialogFragment() {

    private var _binding: DialogAddTaskBinding? = null
    private val binding get() = _binding!!
    private var listener: IAddTaskListener? = null


    private val viewModel by viewModels<AddTaskDialogViewModel> {
        AddTaskDialogViewModel.Factory(requireActivity().application)
    }

    private lateinit var subtaskAdapter: SubtaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAddTaskBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.dialogAddTaskCalendarBtn.setOnClickListener { calendarBtnOnClick() }
        binding.dialogAddTaskAddBtn.setOnClickListener { addTaskBtnOnClick() }
        binding.dialogAddTaskCategoryBtn.setOnClickListener { showCategoryMenu(it) }
        subtaskAdapter = SubtaskAdapter(object : ISubtaskListener {
            override fun onCheck(subtask: Subtask) {
                viewModel.updateSubtask(subtask)
            }

            override fun removeButtonOnClick(subtask: Subtask) {
                val id = viewModel.deleteSubtask(subtask)
                if (id != -1) subtaskAdapter.notifyItemRemoved(id)
            }

            override fun afterEditName(subtask: Subtask) {
                if (subtask.name == "") {
                    removeButtonOnClick(subtask)
                } else {
                    viewModel.updateSubtask(subtask)
                }
            }
        })
        viewModel.setSubtaskListOnChangeListener {
            subtaskAdapter.setData(it)
        }
        binding.dialogAddTaskSubtaskRcv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = subtaskAdapter
        }
        binding.dialogAddTaskSubtaskBtn.setOnClickListener {
            subtaskAdapter.notifyItemInserted(viewModel.newSubtask())
        }
    }

    private fun addTaskBtnOnClick() {
        val name: String = binding.dialogAddTaskNameTxt.editText?.text.toString()
        if (name == "") {
            Toast.makeText(context, "Your task's name shouldn't be empty!", Toast.LENGTH_SHORT)
                .show()
        } else {
            binding.dialogAddTaskSubtaskRcv.clearFocus()
            viewModel.insertTaskWithSubtasks(requireContext(), name)
            dismiss()
        }
    }

    private fun calendarBtnOnClick() {
        val dateDialog = DatetimePickerDialogFragment.newInstance(object : IDatetimePickerListener {
            override fun onFinish(result: Int, date: Date?, repeat: String?) {
                viewModel.date = date
                viewModel.repeat = repeat
            }
        })
        dateDialog.show(parentFragmentManager, "datetime_picker_dialog")
    }

    private fun showCategoryMenu(view: View) {
        val popupMenu = PopupMenu(context, view, Gravity.TOP)
        popupMenu.menu.add(0, 0, 0, getString(R.string.none))
        viewModel.getAllCategory().observe(viewLifecycleOwner) { list ->
            list.forEachIndexed { index, category ->
                popupMenu.menu.add(0, category.id, index + 1, category.name)
            }
        }
        popupMenu.setOnMenuItemClickListener {
            binding.dialogAddTaskCategoryBtn.text = it.title
            if (it.title == getString(R.string.none) && it.itemId == 0) {
                viewModel.categoryId = null
                true
            } else {
                viewModel.categoryId = it.itemId
                true
            }
        }
        popupMenu.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDismiss(dialog: DialogInterface) {
        listener?.onFinish(viewModel.result, viewModel.categoryId)
        super.onDismiss(dialog)
    }

    companion object {
        fun newInstance(listener: IAddTaskListener) = AddTaskDialogFragment().also {
            it.dialog?.setCanceledOnTouchOutside(true).apply {
                it.listener = listener
            }
        }
    }
}