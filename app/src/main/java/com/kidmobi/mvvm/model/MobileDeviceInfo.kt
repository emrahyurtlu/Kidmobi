package com.kidmobi.mvvm.model

import android.os.Build
import java.io.Serializable
import javax.inject.Inject

data class MobileDeviceInfo @Inject constructor(
    var brand: String,
    var device: String,
    var model: String,
    var product: String,
    var host: String,
    var time: Long,
    var sdk: Int,
    var increment: String,
    var securityPatch: String
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
    }
}