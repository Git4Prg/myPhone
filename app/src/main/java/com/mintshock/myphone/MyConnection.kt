package com.mintshock.myphone.services

import android.telecom.Connection
import android.telecom.DisconnectCause

class MyConnection : Connection() {
    init {
        setConnectionCapabilities(CAPABILITY_SUPPORT_HOLD or CAPABILITY_HOLD)
        setAudioModeIsVoip(true)
    }

    override fun onDisconnect() {
        setDisconnected(DisconnectCause(DisconnectCause.LOCAL))
        destroy()
    }

    override fun onAbort() {
        setDisconnected(DisconnectCause(DisconnectCause.ERROR))
        destroy()
    }
}
