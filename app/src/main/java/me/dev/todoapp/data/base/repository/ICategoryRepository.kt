package me.dev.todoapp.data.base.repository

import androidx.lifecycle.LiveData
import me.dev.todoapp.data.base.model.Category

interface ICategoryRepository {
    suspend fun insertCategory(category: Category)
    suspend fun updateCategory(category: Category)
    suspend fun deleteCategory(category: Category)
    fun getCategory(id: Int): LiveData<Category>
    fun getAllCategory(): LiveData<List<Category>>
}