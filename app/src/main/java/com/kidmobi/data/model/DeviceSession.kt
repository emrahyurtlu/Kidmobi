package com.kidmobi.data.model

import java.io.Serializable
import java.util.*

data class DeviceSession(
    var sessionCreatorDeviceId: String = "",
    var sessionOwnerDeviceId: String = "",
    var sessionStart: Date? = null,
    var sessionEnd: Date? = null,
) : BaseModel, Serializable
