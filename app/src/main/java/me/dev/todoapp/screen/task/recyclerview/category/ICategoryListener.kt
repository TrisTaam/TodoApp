package me.dev.todoapp.screen.task.recyclerview.category

import me.dev.todoapp.data.base.model.Category

interface ICategoryListener {
    fun afterEditName(category: Category)
    fun removeOnClick(category: Category)
}