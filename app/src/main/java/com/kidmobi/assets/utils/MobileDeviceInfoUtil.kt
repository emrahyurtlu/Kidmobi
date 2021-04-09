package com.kidmobi.assets.utils

import android.os.Build
import com.kidmobi.mvvm.model.MobileDeviceInfo

fun MobileDeviceInfo.initialize(): MobileDeviceInfo {
    return MobileDeviceInfo(
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