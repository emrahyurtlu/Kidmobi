package com.kidmobi.mvvm.model

import android.os.Build
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
) : Serializable, BaseModel {
    companion object {
        fun init() = MobileDeviceInfo(
            brand = Build.BRAND,
            device = Build.DEVICE,
            model = Build.MODEL,
            product = Build.PRODUCT,
            host = Build.HOST,
            time = Build.TIME,
            sdk = Build.VERSION.SDK_INT,
            increment = Build.VERSION.INCREMENTAL,
            securityPatch = Build.VERSION.SECURITY_PATCH
        )

        fun blank() = MobileDeviceInfo(
            brand = "",
            device = "",
            model = "",
            product = "",
            host = "",
            time = 0,
            sdk = 0,
            increment = "",
            securityPatch = ""
        )
    }
}