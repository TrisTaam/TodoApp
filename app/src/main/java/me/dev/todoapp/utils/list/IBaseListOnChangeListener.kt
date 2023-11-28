package me.dev.todoapp.utils.list

import me.dev.todoapp.data.base.model.Identifiable

interface IBaseListOnChangeListener<E : Identifiable> {
    fun onChange(newList: BaseList<E>)
}