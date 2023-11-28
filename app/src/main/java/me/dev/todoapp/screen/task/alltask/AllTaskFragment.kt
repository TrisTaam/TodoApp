package me.dev.todoapp.screen.task.alltask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.OnBackPressedCallback
import androidx.annotation.MenuRes
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import me.dev.todoapp.R
import me.dev.todoapp.data.base.model.Task
import me.dev.todoapp.databinding.FragmentAllTaskBinding
import me.dev.todoapp.screen.MainActivityViewModel
import me.dev.todoapp.screen.task.recyclerview.task.ITaskOnCheckListener
import me.dev.todoapp.screen.task.recyclerview.task.ITaskOnClickListener
import me.dev.todoapp.screen.task.recyclerview.task.TaskAdapter
import me.dev.todoapp.screen.task.taskdetail.TaskDetailFragment

class AllTaskFragment : Fragment() {

    private var _binding: FragmentAllTaskBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<MainActivityViewModel> {
        MainActivityViewModel.Factory(requireActivity().application)
    }

    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllTaskBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.allTaskMenuBtn.setOnClickListener {
            showOverflowMenu(
                it,
                R.menu.alltask_overflow_menu
            )
        }
        initTaskRecyclerView()
        initCategoryChipGroup()
        initSearchBar()
    }

    private fun initSearchBar() {
        binding.allTaskSearchBackBtn.setOnClickListener { hideSearchBar() }
        binding.allTaskSearchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (p0 == null) return false
                viewModel.setTaskFilter(p0, viewLifecycleOwner)
                return true
            }
        })
    }

    private fun initTaskRecyclerView() {
        taskAdapter = TaskAdapter()
        binding.allTaskRcv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = taskAdapter
        }
        taskAdapter.apply {
            setOnCheckListener(object : ITaskOnCheckListener {
                override fun onCheck(status: Boolean, task: Task) {
                    viewModel.updateTask(task.also { it.status = status })
                }
            })
            setOnClickListener(object : ITaskOnClickListener {
                override fun onClick(task: Task) {
                    showTaskDetail(task)
                }
            })
        }
        viewModel.tasks.observe(viewLifecycleOwner) { taskAdapter.setDataWithDiffUtil(it) }
    }

    private fun initCategoryChipGroup() {
        viewModel.getAllCategory().observe(viewLifecycleOwner) { list ->
            // Create menu
            binding.allTaskChipGroup.removeAllViews()
            val allChip: Chip = Chip(context).apply {
                id = 0
                text = getString(R.string.all)
                isCheckable = true
            }
            binding.allTaskChipGroup.addView(allChip)
            list.forEach {
                val chip: Chip = Chip(context).apply {
                    id = it.id
                    text = it.name
                    isCheckable = true
                }
                binding.allTaskChipGroup.addView(chip)
            }
            // Handle click and category change event
            binding.allTaskChipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
                if (checkedIds.size == 0) return@setOnCheckedStateChangeListener
                val id = checkedIds[0]
                viewModel.setCategoryFilter(id, viewLifecycleOwner)
            }
            viewModel.categoryId.observe(viewLifecycleOwner) observeCategoryId@{ id ->
                val checkedIds = binding.allTaskChipGroup.checkedChipIds
                if (checkedIds.size == 0) {
                    binding.allTaskChipGroup.check(id)
                    return@observeCategoryId
                }
                val chipId = checkedIds[0]
                if (chipId == id) return@observeCategoryId
                else binding.allTaskChipGroup.check(id)
            }
        }
    }

    private fun showTaskDetail(task: Task) {
        val navController = findNavController()
        navController.navigate(
            R.id.action_allTaskFragment_to_taskDetailFragment, bundleOf(
                TaskDetailFragment.ARG_TASK_ID to task.id
            )
        )
    }

    private fun showOverflowMenu(view: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(context, view)
        popup.menuInflater.inflate(menuRes, popup.menu)
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.alltask_overflow_manage_categories_menu -> {
                    findNavController().navigate(R.id.action_allTaskFragment_to_manageCategoryFragment)
                    true
                }

                R.id.alltask_overflow_search_menu -> {
                    showSearchBar()
                    true
                }

                R.id.alltask_overflow_sort_by_creation_time -> {
                    viewModel.sortCurrentList(
                        MainActivityViewModel.TaskSort.CREATION_TIME,
                        viewLifecycleOwner
                    )
                    true
                }

                R.id.alltask_overflow_sort_by_date_time -> {
                    viewModel.sortCurrentList(
                        MainActivityViewModel.TaskSort.DATE_TIME,
                        viewLifecycleOwner
                    )
                    true
                }

                R.id.alltask_overflow_sort_by_alpha_az -> {
                    viewModel.sortCurrentList(
                        MainActivityViewModel.TaskSort.A_Z,
                        viewLifecycleOwner
                    )
                    true
                }

                R.id.alltask_overflow_sort_by_alpha_za -> {
                    viewModel.sortCurrentList(
                        MainActivityViewModel.TaskSort.Z_A,
                        viewLifecycleOwner
                    )
                    true
                }

                else -> {
                    false
                }
            }
        }
        popup.show()
    }

    private fun showSearchBar() {
        binding.allTaskChipGroup.visibility = View.INVISIBLE
        binding.allTaskMenuBtn.visibility = View.INVISIBLE
        binding.allTaskSearchview.visibility = View.VISIBLE
        binding.allTaskSearchBackBtn.visibility = View.VISIBLE
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (binding.allTaskSearchview.visibility == View.VISIBLE) {
                        hideSearchBar()
                        this.remove()
                    } else {
                        this.remove()
                        handleOnBackPressed()
                    }
                }
            }
        )
    }

    private fun hideSearchBar() {
        binding.allTaskSearchview.setQuery("", false)
        binding.allTaskSearchview.visibility = View.GONE
        binding.allTaskSearchBackBtn.visibility = View.GONE
        binding.allTaskChipGroup.visibility = View.VISIBLE
        binding.allTaskMenuBtn.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}