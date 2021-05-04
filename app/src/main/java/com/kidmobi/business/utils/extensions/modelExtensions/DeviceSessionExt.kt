package com.kidmobi.business.utils.extensions.modelExtensions

import com.kidmobi.mvvm.model.DeviceSession
import java.util.*

fun DeviceSession.init() = DeviceSession(
    sessionCreatorDeviceId = "",
    sessionOwnerDeviceId = "",
    sessionStart = null,
    sessionEnd = null
)

fun DeviceSession.isNull() = this.sessionCreatorDeviceId.isEmpty()
fun DeviceSession.isNotNull() = this.sessionCreatorDeviceId.isNotEmpty()

fun DeviceSession.isValid(): Boolean {
    val calendar = Calendar.getInstance()
    return this.isNotNull() && calendar.time < this.sessionEnd
}

fun DeviceSession.isInvalid(): Boolean {
    val calendar = Calendar.getInstance()
    return this.sessionEnd == null || calendar.time >= this.sessionEnd
}