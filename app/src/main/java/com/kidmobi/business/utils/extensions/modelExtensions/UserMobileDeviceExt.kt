package com.kidmobi.business.utils.extensions.modelExtensions

import com.kidmobi.mvvm.model.ManagedDevice

fun ManagedDevice.thisDevice() = ManagedDevice()

fun ManagedDevice.isNull() = this.devices.size == 0