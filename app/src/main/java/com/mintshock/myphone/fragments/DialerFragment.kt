package com.mintshock.myphone.fragments

import android.Manifest.permission.CALL_PHONE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.mintshock.myphone.R

class DialerFragment : Fragment() {
    private lateinit var tvPhoneNumber: TextView
    private lateinit var btnCall: Button
    private lateinit var btnDelete: Button
    private val phoneNumber = StringBuilder()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_dialer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber)
        btnCall = view.findViewById(R.id.btnCall)
        btnDelete = view.findViewById(R.id.btnDelete)

        val buttons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5,
            R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btnStar, R.id.btnHash
        )

        buttons.forEach { id ->
            view.findViewById<Button>(id).setOnClickListener {
                phoneNumber.append((it as Button).text)
                tvPhoneNumber.text = phoneNumber.toString()
            }
        }

        btnDelete.setOnClickListener {
            if (phoneNumber.isNotEmpty()) {
                phoneNumber.deleteCharAt(phoneNumber.length - 1)
                tvPhoneNumber.text = phoneNumber.toString()
            }
        }

        btnCall.setOnClickListener {
            makeCall()
        }
    }

    private fun makeCall() {
        val number = phoneNumber.toString().trim()

        if (number.isEmpty()) {
            Toast.makeText(requireContext(), "Enter a number to call", Toast.LENGTH_SHORT).show()
            return
        }

        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:$number")

        if (ContextCompat.checkSelfPermission(requireContext(), CALL_PHONE)
            == PackageManager.PERMISSION_GRANTED) {
            startActivity(callIntent)
        } else {
            requestPermissions(arrayOf(CALL_PHONE), REQUEST_CALL_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeCall()  // Retry calling after permission is granted
            } else {
                Toast.makeText(requireContext(), "Call permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val REQUEST_CALL_PERMISSION = 101
    }
}
