package me.dev.todoapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import me.dev.todoapp.data.base.repository.IRepositoryBuilder
import me.dev.todoapp.data.local.LocalDatabase
import me.dev.todoapp.data.local.repository.LocalRepositoryBuilder

class TodoApplication : Application() {

    private var _repositoryBuilder: IRepositoryBuilder? = null
    val repositoryBuilder: IRepositoryBuilder get() = _repositoryBuilder!!

    override fun onCreate() {
        super.onCreate()
        LocalDatabase.getInstance(applicationContext)
        _repositoryBuilder = LocalRepositoryBuilder(applicationContext)
        initNotificationChannel()
    }

    private fun initNotificationChannel() {
        val channel =
            NotificationChannel("alertChannel", "Alert", NotificationManager.IMPORTANCE_HIGH)
        channel.description = "Alert your task"
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    override fun onTerminate() {
        super.onTerminate()
        _repositoryBuilder = null
    }
}