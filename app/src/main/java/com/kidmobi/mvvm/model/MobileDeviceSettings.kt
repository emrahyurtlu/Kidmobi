package com.kidmobi.mvvm.model

import java.io.Serializable

data class MobileDeviceSettings constructor(
    var brightnessLevel: Float = 150F,
    var soundLevel: Float = 8F
) : Serializable, BaseModel {
    companion object {
        fun init() = MobileDeviceSettings(brightnessLevel = 150F, soundLevel = 8F)
    }
}