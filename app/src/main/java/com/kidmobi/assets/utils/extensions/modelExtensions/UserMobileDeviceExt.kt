package com.kidmobi.assets.utils.extensions.modelExtensions

import com.kidmobi.mvvm.model.ManagedDevice

fun ManagedDevice.init() = ManagedDevice()

fun ManagedDevice.isNull() = this.devices.size == 0