package com.kidmobi.mvvm.model

import java.io.Serializable

data class UserMobileDevice constructor(
    var devices: MutableList<String>
) : Serializable, BaseModel {
    companion object {
        fun init() = UserMobileDevice(devices = mutableListOf())
    }
}