package com.kidmobi.mvvm.model

import com.kidmobi.assets.enums.UserType
import java.io.Serializable
import java.util.*
import javax.inject.Inject

data class MobileDevice @Inject constructor(
    var deviceId: String,
    var deviceImageUrl: Int,
    var deviceOwnerName: String,
    var deviceOwnerImageUrl: String,
    var deviceOwnerUid: String,
    var deviceOwnerEmail: String,
    var info: MobileDeviceInfo,
    var settings: MobileDeviceSettings,
    var createdAt: Date?,
    var updatedAt: Date?,
    var userType: UserType
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
            settings = MobileDeviceSettings(brightnessLevel = 150F, soundLevel = 8F),
            createdAt = null,
            updatedAt = null,
            userType = UserType.UserUnknown
        )
    }
}