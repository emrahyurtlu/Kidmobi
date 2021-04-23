package com.kidmobi.assets.utils.extensions

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.kidmobi.assets.utils.extensions.modelExtensions.init
import com.kidmobi.mvvm.model.DeviceSession
import com.kidmobi.mvvm.model.MobileDevice
import com.kidmobi.mvvm.model.UserMobileDevice

fun DocumentSnapshot.toUserMobileDevice() =
    this.toObject<UserMobileDevice>() ?: UserMobileDevice().init()

fun DocumentSnapshot.toMobileDevice() = this.toObject<MobileDevice>() ?: MobileDevice().init()

fun QuerySnapshot.toMobileDeviceList() = this.toObjects<MobileDevice>().toMutableList()

fun DocumentSnapshot.toDeviceSession() = this.toObject<DeviceSession>() ?: DeviceSession().init()

fun QuerySnapshot.toDeviceSessionList() = this.toObjects<DeviceSession>().toMutableList()