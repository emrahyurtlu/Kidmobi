package com.kidmobi.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.kidmobi.assets.enums.UserType
import com.kidmobi.assets.repositories.MobileDeviceRepo
import com.kidmobi.assets.utils.initialize
import com.kidmobi.mvvm.model.MobileDevice
import com.kidmobi.mvvm.model.MobileDeviceInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MobileDeviceViewModel @Inject constructor() : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val mobileDeviceRepo: MobileDeviceRepo = MobileDeviceRepo()
    @Inject
    lateinit var device: MobileDevice
    @Inject
    lateinit var mobileDeviceInfo: MobileDeviceInfo

    suspend fun saveDeviceInitially(uniqueDeviceId: String) {
        auth.currentUser?.let { user ->
            val now = Calendar.getInstance()
            val deviceInfo = mobileDeviceInfo.initialize()

            device.apply {
                deviceId = uniqueDeviceId
                info = deviceInfo
                createdAt = now.time
                updatedAt = now.time
                deviceOwnerName = user.displayName.toString()
                deviceOwnerImageUrl = user.photoUrl.toString()
                deviceOwnerUid = user.uid
                deviceOwnerEmail = user.email.toString()
            }

            for (profile in user.providerData) {
                // Id of the provider (ex: google.com)
                val providerId = profile.providerId
            }


            device.userType = when (user.providerId) {
                "google.com" -> UserType.UserFromGoogle
                "facebook.com" -> UserType.UserFromFacebook
                "firebase" -> UserType.UserAnonymous
                else -> UserType.UserUnknown
            }

            val docExist = mobileDeviceRepo.docExists(uniqueDeviceId)
            if (!docExist)
                mobileDeviceRepo.add(device)
        }
    }
}