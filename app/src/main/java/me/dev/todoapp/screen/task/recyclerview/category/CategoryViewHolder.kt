package me.dev.todoapp.screen.task.recyclerview.category

import me.dev.todoapp.data.base.model.Category
import me.dev.todoapp.databinding.ViewholderCategoryBinding
import me.dev.todoapp.utils.recyclerview.BaseViewHolder

class CategoryViewHolder(
    override val binding: ViewholderCategoryBinding,
    private val listener: ICategoryListener
) : BaseViewHolder<Category, ViewholderCategoryBinding>(binding) {

    override fun bind(item: Category) {
        binding.categoryName.setText(item.name)
        binding.categoryName.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus)
                listener.afterEditName(
                    item.copy(
                        name = binding.categoryName.text.toString().trim()
                    )
                )
        }
        binding.categoryRemoveBtn.setOnClickListener {
            listener.removeOnClick(item)
        }
    }
}