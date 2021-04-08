package com.kidmobi.mvvm.model

import com.kidmobi.R
import com.kidmobi.assets.enums.UserType
import java.io.Serializable
import java.util.*

data class MobileDevice(
    var deviceId: String = "",
    var deviceImageUrl: Int = 0,
    var deviceOwnerName: String = "",
    var deviceOwnerImageUrl: String = "",
    var deviceOwnerUid: String = "",
    var deviceOwnerEmail: String = "",
    var info: MobileDeviceInfo = MobileDeviceInfo(),
    var settings: MobileDeviceSettings = MobileDeviceSettings(),
    var createdAt: Date? = null,
    var updatedAt: Date? = null,
    var userType: UserType? = null
) : Serializable, BaseModel