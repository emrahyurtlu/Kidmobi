package com.kidmobi.data.model

import com.kidmobi.business.utils.enums.UserType
import com.kidmobi.business.utils.extensions.modelExtensions.blank
import java.io.Serializable
import java.util.*

data class MobileDevice constructor(
    var deviceId: String = "",
    var deviceImageUrl: Int = 0,
    var deviceOwnerName: String = "",
    var deviceOwnerImageUrl: String = "",
    var deviceOwnerUid: String = "",
    var deviceOwnerEmail: String = "",
    var info: MobileDeviceInfo = MobileDeviceInfo().blank(),
    var apps: MutableList<InstalledApp> = mutableListOf(),
    var runningApps: MutableList<InstalledApp> = mutableListOf(),
    var settings: MobileDeviceSettings = MobileDeviceSettings(),
    var session: DeviceSession = DeviceSession(),
    var createdAt: Date? = null,
    var updatedAt: Date? = null,
    var userType: UserType = UserType.UserUnknown
) : BaseModel, Serializable