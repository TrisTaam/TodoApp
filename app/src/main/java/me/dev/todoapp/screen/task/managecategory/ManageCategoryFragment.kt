package me.dev.todoapp.screen.task.managecategory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import me.dev.todoapp.data.base.model.Category
import me.dev.todoapp.databinding.FragmentManageCategoryBinding
import me.dev.todoapp.screen.task.recyclerview.category.CategoryAdapter
import me.dev.todoapp.screen.task.recyclerview.category.ICategoryListener

class ManageCategoryFragment : Fragment() {

    private var _binding: FragmentManageCategoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ManageCategoryViewModel> {
        ManageCategoryViewModel.Factory(requireActivity().application)
    }
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageCategoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Toolbar
        binding.manageCategoryToolbar.setupWithNavController(findNavController())
        // Category Recycler View
        categoryAdapter = CategoryAdapter(object : ICategoryListener {
            override fun afterEditName(category: Category) {
                viewModel.updateCategory(category)
            }

            override fun removeOnClick(category: Category) {
                viewModel.deleteCategory(category)
            }
        })
        binding.manageCategoryRcv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = categoryAdapter
        }
        viewModel.getAllCategory().observe(viewLifecycleOwner) {
            categoryAdapter.setDataWithDiffUtil(it)
        }
        // New Category Button
        binding.manageCategoryNewCategoryBtn.setOnClickListener {
            viewModel.newCategory()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}