package com.mintshock.myphone.receivers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.mintshock.myphone.R

class MissedCallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val state = intent.getStringExtra("state")
        val incomingNumber = intent.getStringExtra("incoming_number")

        if (state == "RINGING") {
            showMissedCallNotification(context, incomingNumber ?: "Unknown")
        }
    }

    private fun showMissedCallNotification(context: Context, number: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel("missed_calls", "Missed Calls", NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(context, "missed_calls")
            .setSmallIcon(R.drawable.call_missed)
            .setContentTitle("Missed Call")
            .setContentText("Missed call from $number")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(1, notification)
    }
}
