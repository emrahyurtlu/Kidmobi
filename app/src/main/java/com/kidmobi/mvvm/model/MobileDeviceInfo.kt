package com.kidmobi.mvvm.model

import java.io.Serializable
import javax.inject.Inject

data class MobileDeviceInfo @Inject constructor(
    var brand: String?,
    var device: String?,
    var model: String?,
    var product: String?,
    var host: String?,
    var time: Long?,
    var sdk: Int?,
    var increment: String?,
    var securityPatch: String?
) : Serializable, BaseModel