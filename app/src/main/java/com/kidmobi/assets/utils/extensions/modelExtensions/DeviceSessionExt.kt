package com.kidmobi.assets.utils.extensions.modelExtensions

import com.kidmobi.mvvm.model.DeviceSession

fun DeviceSession.init() = DeviceSession(
    sessionCreatorDeviceId = "",
    sessionOwnerDeviceId = "",
    sessionStart = null,
    sessionEnd = null,
    done = false
)

fun DeviceSession.isNull() = this.sessionCreatorDeviceId.isEmpty()