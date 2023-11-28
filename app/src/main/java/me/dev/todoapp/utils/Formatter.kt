package me.dev.todoapp.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Formatter {

    enum class DateFormat {
        TIME_AND_DATE, DATE, SHORTEN_DATE, TIME
    }

    fun dateToString(date: Date, dateFormat: DateFormat): String {
        val simpleDateFormat = SimpleDateFormat(
            when (dateFormat) {
                DateFormat.TIME_AND_DATE -> "HH:mm dd/MM/yyyy"
                DateFormat.DATE -> "dd/MM/yyyy"
                DateFormat.SHORTEN_DATE -> "dd/MM"
                DateFormat.TIME -> "HH:mm"
            },
            Locale.US
        )
        return simpleDateFormat.format(date)
    }
}