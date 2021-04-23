package com.kidmobi.assets.utils.extensions.modelExtensions

import com.kidmobi.mvvm.model.UserMobileDevice

fun UserMobileDevice.init() = UserMobileDevice(devices = mutableListOf())
fun UserMobileDevice.isNull() = this.devices.size == 0