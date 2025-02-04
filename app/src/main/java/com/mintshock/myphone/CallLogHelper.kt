package com.mintshock.myphone

import android.content.Context
import android.provider.CallLog
import com.mintshock.myphone.models.CallLogEntry

object CallLogHelper {
    fun getCallLogs(context: Context): List<CallLogEntry> {
        val callLogs = mutableListOf<CallLogEntry>()
        val cursor = context.contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            arrayOf(
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.NUMBER,
                CallLog.Calls.TYPE,
                CallLog.Calls.DATE,
                CallLog.Calls.DURATION
            ),
            null, null,
            CallLog.Calls.DATE + " DESC"
        )

        cursor?.use {
            while (it.moveToNext()) {
                val contactName = it.getString(0) ?: "Unknown"
                val phoneNumber = it.getString(1) ?: "Unknown"
                val callType = it.getInt(2)
                val callDate = it.getLong(3)
                val callDuration = it.getInt(4)

                callLogs.add(CallLogEntry(contactName, phoneNumber, callType, callDate, callDuration))
            }
        }
        return callLogs
    }
}
