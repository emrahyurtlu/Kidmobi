package com.kidmobi.business.utils.extensions

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.kidmobi.business.utils.extensions.modelExtensions.init
import com.kidmobi.mvvm.model.DeviceSession
import com.kidmobi.mvvm.model.ManagedDevice
import com.kidmobi.mvvm.model.MobileDevice

fun DocumentSnapshot.toManagedDevice() =
    this.toObject<ManagedDevice>() ?: ManagedDevice().init()

fun DocumentSnapshot.toMobileDevice() = this.toObject<MobileDevice>() ?: MobileDevice()

fun QuerySnapshot.toMobileDeviceList() = this.toObjects<MobileDevice>().toMutableList()

fun DocumentSnapshot.toDeviceSession() = this.toObject<DeviceSession>() ?: DeviceSession()

fun QuerySnapshot.toDeviceSessionList() = this.toObjects<DeviceSession>().toMutableList()