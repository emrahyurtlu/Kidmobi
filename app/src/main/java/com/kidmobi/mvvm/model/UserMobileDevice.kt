package com.kidmobi.mvvm.model

import java.io.Serializable

data class UserMobileDevice(
    var devices: MutableList<String> = mutableListOf()
) : Serializable, BaseModel