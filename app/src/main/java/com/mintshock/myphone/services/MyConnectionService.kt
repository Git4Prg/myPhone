package com.mintshock.myphone.services

import android.telecom.Connection
import android.telecom.ConnectionRequest
import android.telecom.ConnectionService
import android.telecom.PhoneAccountHandle

class MyConnectionService : ConnectionService() {
    override fun onCreateIncomingConnection(
        phoneAccount: PhoneAccountHandle?, request: ConnectionRequest
    ): Connection {
        return MyConnection()
    }

    override fun onCreateOutgoingConnection(
        phoneAccount: PhoneAccountHandle?, request: ConnectionRequest
    ): Connection {
        return MyConnection()
    }
}
