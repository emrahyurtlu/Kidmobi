package com.kidmobi.data.model

import java.io.Serializable

data class MobileDeviceInfo constructor(
    var brand: String = "",
    var device: String = "",
    var model: String = "",
    var product: String = "",
    var host: String = "",
    var time: Long = 0,
    var sdk: Int = 0,
    var increment: String = "",
    var securityPatch: String = ""
) : BaseModel, Serializable