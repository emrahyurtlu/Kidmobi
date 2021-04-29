package com.kidmobi.assets.utils.extensions.modelExtensions

import com.kidmobi.mvvm.model.DeviceSession
import java.util.*

fun DeviceSession.init() = DeviceSession(
    sessionCreatorDeviceId = "",
    sessionOwnerDeviceId = "",
    sessionStart = null,
    sessionEnd = null,
    done = false
)

fun DeviceSession.isNull() = this.sessionCreatorDeviceId.isEmpty()

fun DeviceSession.isValid() = run {
    val calendar = Calendar.getInstance()
    calendar.time < this.sessionEnd && !this.done
}