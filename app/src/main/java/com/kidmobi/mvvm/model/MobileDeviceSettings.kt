package com.kidmobi.mvvm.model

import java.io.Serializable
import javax.inject.Inject

data class MobileDeviceSettings @Inject constructor(
    var brightnessLevel: Float?,
    var soundLevel: Float?
) : Serializable, BaseModel