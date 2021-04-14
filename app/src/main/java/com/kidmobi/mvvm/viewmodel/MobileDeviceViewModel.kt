package com.kidmobi.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.kidmobi.assets.enums.UserType
import com.kidmobi.assets.repositories.MobileDeviceRepo
import com.kidmobi.mvvm.model.MobileDevice
import com.kidmobi.mvvm.model.MobileDeviceInfo
import com.kidmobi.mvvm.model.MobileDeviceSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MobileDeviceViewModel @Inject constructor() : ViewModel() {
    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var mobileDeviceRepo: MobileDeviceRepo

    @Inject
    lateinit var device: MobileDevice

    fun saveDeviceInitially(uniqueDeviceId: String) {
        CoroutineScope(Dispatchers.Default).launch {
            val auth = FirebaseAuth.getInstance()
            auth.currentUser?.let { user ->
                val now = Calendar.getInstance()
                device.apply {
                    deviceId = uniqueDeviceId
                    info = MobileDeviceInfo.init()
                    settings = MobileDeviceSettings.init()
                    createdAt = now.time
                    updatedAt = now.time
                    deviceOwnerName = user.displayName.toString()
                    deviceOwnerImageUrl = user.photoUrl.toString()
                    deviceOwnerEmail = user.email.toString()
                    deviceOwnerUid = user.uid
                    userType = when (user.providerId) {
                        "google.com" -> UserType.UserFromGoogle
                        "facebook.com" -> UserType.UserFromFacebook
                        "firebase" -> UserType.UserAnonymous
                        else -> UserType.UserUnknown
                    }
                }

                val docExist = mobileDeviceRepo.docExists(uniqueDeviceId)
                if (!docExist)
                    mobileDeviceRepo.add(device)
            }
        }
    }

    fun getDeviceById(deviceId: String): MobileDevice {
        CoroutineScope(Dispatchers.Default).launch {
            device = mobileDeviceRepo.getById(deviceId)
        }
        return device
    }

    fun updateDevice(device: MobileDevice) {
        val calendar = Calendar.getInstance()
        device.updatedAt = calendar.time
        CoroutineScope(Dispatchers.Default).launch {
            mobileDeviceRepo.update(documentId = device.deviceId, entity = device)
            Timber.d("updateDevice: $device")
        }
    }
}