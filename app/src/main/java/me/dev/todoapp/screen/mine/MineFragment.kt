package me.dev.todoapp.screen.mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import me.dev.todoapp.R
import me.dev.todoapp.databinding.FragmentMineBinding
import me.dev.todoapp.screen.mine.recyclerview.TaskSimpleAdapter


class MineFragment : Fragment() {

    private var _binding: FragmentMineBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MineViewModel> {
        MineViewModel.Factory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMineBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.initObserver(viewLifecycleOwner)
        initUserView()
        initTaskCounter()
        initTaskIn7Days()
        initTaskStats()
    }

    private fun initUserView() {
        val userName = "User"
        val userType = "Local Account"
        binding.mineUserName.text = userName
        binding.mineUserType.text = userType
        binding.mineUserPfp.setImageResource(R.mipmap.ic_launcher)
    }

    private fun initTaskCounter() {
        viewModel.completedTasksCount.observe(viewLifecycleOwner) {
            binding.mineCompletedTaskCountSubtitle.text = it.toString()
        }
        viewModel.pendingTasksCount.observe(viewLifecycleOwner) {
            binding.minePendingTaskCountSubtitle.text = it.toString()
        }
    }

    private fun initTaskIn7Days() {
        val taskSimpleAdapter = TaskSimpleAdapter()
        binding.mineTaskIn7daysRcv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = taskSimpleAdapter
        }
        viewModel.tasksInNext7Days.observe(viewLifecycleOwner) {
            taskSimpleAdapter.setDataWithDiffUtil(it)
        }
    }

    private fun initTaskStats() {
        // Add a lot of colors
        val colors: MutableList<Int> = mutableListOf<Int>().apply {
            addAll(ColorTemplate.COLORFUL_COLORS.toList())
            addAll(ColorTemplate.MATERIAL_COLORS.toList())
        }

        binding.mineTaskStatsPieChart.apply {
            description.isEnabled = false
            setDrawEntryLabels(false)
            setExtraOffsets(5F, 10F, 5F, 5F)
            holeRadius = 58f
            isDrawHoleEnabled = true
            isRotationEnabled = true
        }
        viewModel.taskPieEntries.observe(viewLifecycleOwner) {
            if (it.isEmpty()) return@observe
            binding.mineTaskStatsPieChart.data =
                PieData(
                    PieDataSet(it, "").apply {
                        setDrawIcons(false)
                        sliceSpace = 5f
                        selectionShift = 5f
                        setColors(colors)
                    }
                ).apply {
                    setValueTextSize(0f)
                }
            binding.mineTaskStatsPieChart.apply {
                highlightValue(null)
                invalidate()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}