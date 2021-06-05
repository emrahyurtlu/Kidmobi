package com.kidmobi.business.utils.extensions.modelExtensions

import com.kidmobi.business.utils.enums.UserType
import com.kidmobi.data.model.MobileDevice
import com.kidmobi.data.model.MobileDeviceInfo
import com.kidmobi.data.model.MobileDeviceSettings

fun MobileDevice.thisDevice() = MobileDevice(
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
    settings = MobileDeviceSettings(),
    createdAt = null,
    updatedAt = null,
    userType = UserType.UserUnknown
)

fun MobileDevice.isNull() = this.deviceId.isEmpty()
fun MobileDevice.isNotNull() = this.deviceId.isNotEmpty()

