package com.kidmobi.assets.utils.extensions.modelExtensions

import com.kidmobi.assets.enums.UserType
import com.kidmobi.mvvm.model.MobileDevice
import com.kidmobi.mvvm.model.MobileDeviceInfo
import com.kidmobi.mvvm.model.MobileDeviceSettings

fun MobileDevice.init() = MobileDevice(
    deviceId = "",
    deviceImageUrl = 0,
    deviceOwnerName = "",
    deviceOwnerImageUrl = "",
    deviceOwnerUid = "",
    deviceOwnerEmail = "",
    info = MobileDeviceInfo(
        brand = "",
        device = "",
        model = "",
        product = "",
        host = "",
        time = 0,
        sdk = 0,
        increment = "",
        securityPatch = ""
    ),
    settings = MobileDeviceSettings().init(),
    createdAt = null,
    updatedAt = null,
    userType = UserType.UserUnknown
)

fun MobileDevice.isNull() = this.deviceId.isEmpty()
fun MobileDevice.isNotNull() = this.deviceId.isNotEmpty()

