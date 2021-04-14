package com.kidmobi.mvvm.model

import com.kidmobi.assets.enums.UserType
import java.io.Serializable
import java.util.*

data class MobileDevice constructor(
    var deviceId: String = "",
    var deviceImageUrl: Int = 0,
    var deviceOwnerName: String = "",
    var deviceOwnerImageUrl: String = "",
    var deviceOwnerUid: String = "",
    var deviceOwnerEmail: String = "",
    var info: MobileDeviceInfo = MobileDeviceInfo.blank(),
    var settings: MobileDeviceSettings = MobileDeviceSettings.init(),
    var createdAt: Date? = null,
    var updatedAt: Date? = null,
    var userType: UserType = UserType.UserUnknown
) : Serializable, BaseModel {

    companion object {
        fun init() = MobileDevice(
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
            settings = MobileDeviceSettings.init(),
            createdAt = null,
            updatedAt = null,
            userType = UserType.UserUnknown
        )
    }
}