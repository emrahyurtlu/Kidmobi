package com.kidmobi.mvvm.model

import java.io.Serializable
import java.util.*

data class DeviceSession(
    var sessionCreatorDeviceId: String = "",
    var sessionOwnerDeviceId: String = "",
    var sessionStart: Date? = null,
    var sessionEnd: Date? = null,
    var done: Boolean = false
) : Serializable, BaseModel
