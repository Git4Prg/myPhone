package com.mintshock.myphone.models

data class CallLogEntry(
    val contactName: String?,
    val phoneNumber: String,
    val callType: Int,
    val callDate: Long,
    val callDuration: Int
)
