package com.mintshock.myphone.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast

class OutgoingCallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_NEW_OUTGOING_CALL) {
            val phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER)
            Toast.makeText(context, "Outgoing Call: $phoneNumber", Toast.LENGTH_SHORT).show()

            val newIntent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
            newIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context?.startActivity(newIntent)
        }
    }
}
