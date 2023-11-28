package me.dev.todoapp.utils.list

import me.dev.todoapp.data.base.model.Identifiable

class BaseList<E : Identifiable> : ArrayList<E>(), MutableList<E> {
    private var counter: Int = 0
    private var onChangeListener: IBaseListOnChangeListener<E>? = null
    private var newItemInitializer: INewItemInitializer<E>? = null

    fun setCounter(value: Int) {
        counter = value
    }

    fun setOnChangeListener(listener: IBaseListOnChangeListener<E>) {
        this.onChangeListener = listener
    }

    fun setOnChangeListener(listener: (newList: BaseList<E>) -> Unit) {
        this.onChangeListener = object : IBaseListOnChangeListener<E> {
            override fun onChange(newList: BaseList<E>) {
                listener.invoke(newList)
            }
        }
    }

    fun setNewItemInitializer(initializer: INewItemInitializer<E>) {
        this.newItemInitializer = initializer
    }

    fun setNewItemInitializer(initializer: (id: Int) -> E) {
        this.newItemInitializer = object : INewItemInitializer<E> {
            override fun init(id: Int): E {
                return initializer.invoke(id)
            }
        }
    }

    fun insertNewItem(): Int {
        if (newItemInitializer == null) throw NoInitializerFound()
        val item: E = newItemInitializer!!.init(counter)
        add(item)
        counter++
        onChangeListener?.onChange(this)
        return size - 1
    }

    fun deleteAndGetIndex(item: E): Int {
        val id: Int = indexOfFirst { it.id == item.id }
        if (id == -1) return -1
        removeAt(id)
        onChangeListener?.onChange(this)
        return id
    }

    fun updateAndGetIndex(item: E): Int {
        val id: Int = indexOfFirst { it.id == item.id }
        if (id == -1) return -1
        this[id] = item
        onChangeListener?.onChange(this)
        return id
    }

    class NoInitializerFound : Exception()

}