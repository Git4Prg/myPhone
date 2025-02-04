package com.mintshock.myphone

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telecom.TelecomManager
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class IncomingCallActivity : AppCompatActivity() {

    private lateinit var btnAccept: Button
    private lateinit var btnReject: Button

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incoming_call_screen)

        val incomingNumber = intent.getStringExtra("incoming_number")
        findViewById<TextView>(R.id.txtCallerNumber).text = incomingNumber ?: "Unknown"

        btnAccept = findViewById(R.id.btnAccept)
        btnReject = findViewById(R.id.btnReject)

        btnAccept.setOnClickListener {
            acceptCall()
        }

        btnReject.setOnClickListener {
            rejectCall()
        }
    }

    private fun acceptCall() {
        val telecomManager = getSystemService(TELECOM_SERVICE) as TelecomManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ANSWER_PHONE_CALLS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        telecomManager.acceptRingingCall()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun rejectCall() {
        val telecomManager = getSystemService(TELECOM_SERVICE) as TelecomManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ANSWER_PHONE_CALLS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        telecomManager.endCall()
        finish()
    }
}
