package com.mintshock.myphone.fragments

import android.Manifest.permission.READ_CALL_LOG
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.mintshock.myphone.CallLogHelper
import com.mintshock.myphone.R

class CallLogsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_call_logs, container, false)

        if (ContextCompat.checkSelfPermission(requireContext(), READ_CALL_LOG)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(READ_CALL_LOG),
                101
            )
        } else {
            loadCallLogs()
        }
        return view
    }

    private fun loadCallLogs() {
        val callLogs = CallLogHelper.getCallLogs(requireContext())
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadCallLogs()
        } else {
            Toast.makeText(requireContext(), "Permission Denied!", Toast.LENGTH_SHORT).show()
        }
    }
}

