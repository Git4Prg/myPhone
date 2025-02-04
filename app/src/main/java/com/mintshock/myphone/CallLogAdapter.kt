package com.mintshock.myphone

import android.provider.CallLog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mintshock.myphone.models.CallLogEntry
import java.text.SimpleDateFormat
import java.util.*

class CallLogAdapter(private val callLogs: List<CallLogEntry>) :
    RecyclerView.Adapter<CallLogAdapter.CallLogViewHolder>() {

    class CallLogViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvContactName: TextView = view.findViewById(R.id.tvContactName)
        val tvPhoneNumber: TextView = view.findViewById(R.id.tvPhoneNumber)
        val tvCallDate: TextView = view.findViewById(R.id.tvCallDate)
        val tvCallDuration: TextView = view.findViewById(R.id.tvCallDuration)
        val ivCallType: ImageView = view.findViewById(R.id.ivCallType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallLogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_call_log, parent, false)
        return CallLogViewHolder(view)
    }

    override fun onBindViewHolder(holder: CallLogViewHolder, position: Int) {
        val call = callLogs[position]

        holder.tvContactName.text = call.contactName ?: "Unknown"
        holder.tvPhoneNumber.text = call.phoneNumber
        holder.tvCallDate.text = formatDate(call.callDate)
        holder.tvCallDuration.text = formatDuration(call.callDuration)

        holder.ivCallType.setImageResource(
            when (call.callType) {
                CallLog.Calls.INCOMING_TYPE -> R.drawable.call_incoming
                CallLog.Calls.OUTGOING_TYPE -> R.drawable.call_made
                CallLog.Calls.MISSED_TYPE -> R.drawable.call_missed
                else -> R.drawable.call_received
            }
        )
    }

    override fun getItemCount(): Int = callLogs.size

    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    private fun formatDuration(durationInSeconds: Int): String {
        val minutes = durationInSeconds / 60
        val seconds = durationInSeconds % 60
        return "$minutes min $seconds sec"
    }
}
