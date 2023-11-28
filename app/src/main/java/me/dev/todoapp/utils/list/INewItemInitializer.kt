package me.dev.todoapp.utils.list

interface INewItemInitializer<E> {
    fun init(id: Int): E
}