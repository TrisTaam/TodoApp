package me.dev.todoapp.screen.task.dialog.datetimepicker

import java.util.Date

interface IDatetimePickerListener {
    fun onFinish(result: Int, date: Date?, repeat: String?)
}