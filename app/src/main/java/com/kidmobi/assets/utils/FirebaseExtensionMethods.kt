package com.kidmobi.assets.utils

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.kidmobi.mvvm.model.MobileDevice
import com.kidmobi.mvvm.model.UserMobileDevice

fun DocumentSnapshot.toUserMobileDevice() = this.toObject<UserMobileDevice>() ?: UserMobileDevice()
fun DocumentSnapshot.toMobileDevice() = this.toObject<MobileDevice>() ?: MobileDevice()

fun QuerySnapshot.toMobileDeviceList() = this.toObjects<MobileDevice>().toMutableList()