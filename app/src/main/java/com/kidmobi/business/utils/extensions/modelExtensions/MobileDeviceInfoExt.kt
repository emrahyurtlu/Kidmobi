package com.kidmobi.business.utils.extensions.modelExtensions

import android.os.Build
import com.kidmobi.mvvm.model.MobileDeviceInfo

fun MobileDeviceInfo.init() = MobileDeviceInfo(
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

fun MobileDeviceInfo.blank() = MobileDeviceInfo(
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

fun MobileDeviceInfo.isNull() = this.brand.isEmpty()