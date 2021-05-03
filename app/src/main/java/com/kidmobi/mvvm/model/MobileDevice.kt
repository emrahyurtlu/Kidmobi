package com.kidmobi.mvvm.model

import com.kidmobi.business.enums.UserType
import com.kidmobi.business.utils.extensions.modelExtensions.blank
import com.kidmobi.business.utils.extensions.modelExtensions.init
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
    var settings: MobileDeviceSettings = MobileDeviceSettings().init(),
    var createdAt: Date? = null,
    var updatedAt: Date? = null,
    var userType: UserType = UserType.UserUnknown
) : Serializable, BaseModel