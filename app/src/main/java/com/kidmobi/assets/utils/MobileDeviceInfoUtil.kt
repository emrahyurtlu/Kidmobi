package com.kidmobi.assets.utils

import android.os.Build
import com.kidmobi.mvvm.model.MobileDeviceInfo
import com.mobicon.android.mvvm.view.fragment.DeviceIdentityFragment

/*class MobileDeviceInfoUtil {

    companion object {
        fun fillObject(device: MobileDeviceInfo): MobileDeviceInfo {
            device.brand = Build.BRAND
            device.device = Build.DEVICE
            device.model = Build.MODEL
            device.product = Build.PRODUCT
            device.host = Build.HOST
            device.time = Build.TIME
            device.sdk = Build.VERSION.SDK_INT
            device.increment = Build.VERSION.INCREMENTAL
            device.securityPatch = Build.VERSION.SECURITY_PATCH

            return device
        }
    }
}*/

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