package me.dev.todoapp.utils.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import me.dev.todoapp.utils.Notification

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val bundle = intent?.getBundleExtra("bundle")
        val title = bundle?.getString("title")
        val message = bundle?.getString("message")
        Notification.createNotification(context!!, title!!, message!!)
    }
}