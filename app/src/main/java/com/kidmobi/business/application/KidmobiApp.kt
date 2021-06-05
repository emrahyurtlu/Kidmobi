package com.kidmobi.business.application

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.BuildConfig
import com.google.firebase.firestore.FirebaseFirestore
import com.kidmobi.business.utils.SharedPrefsUtil
import com.kidmobi.business.utils.enums.DbCollection
import com.kidmobi.business.utils.enums.UserType
import com.kidmobi.business.utils.extensions.modelExtensions.thisDevice
import com.kidmobi.business.utils.logs.DebugTree
import com.kidmobi.business.utils.logs.ReleaseTree
import com.kidmobi.data.model.MobileDevice
import com.kidmobi.data.model.MobileDeviceInfo
import com.kidmobi.data.model.MobileDeviceSettings
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject


@HiltAndroidApp
class KidmobiApp : Application() {

    @Inject
    lateinit var sharedPrefsUtil: SharedPrefsUtil


    override fun onCreate() {
        super.onCreate()
        timberSettings()
        saveDevice(sharedPrefsUtil.getDeviceId())
    }

    private fun timberSettings() {
        if (BuildConfig.DEBUG)
            Timber.plant(DebugTree())
        else
            Timber.plant(ReleaseTree())
    }

    private fun saveDevice(uniqueDeviceId: String) {
        Timber.d(uniqueDeviceId)
        CoroutineScope(Dispatchers.Default).launch {
            val device = MobileDevice()
            val auth = FirebaseAuth.getInstance()
            val db = FirebaseFirestore.getInstance()
            auth.currentUser?.let { user ->
                val now = Calendar.getInstance()
                device.apply {
                    deviceId = uniqueDeviceId
                    info = MobileDeviceInfo().thisDevice()
                    settings = MobileDeviceSettings()
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
                val docRef = db.collection(DbCollection.MobileDevices.name).document(uniqueDeviceId)
                docRef.get().addOnSuccessListener {
                    if (!it.exists()) {
                        docRef.set(device)
                        Timber.d("Current device is saved!")
                    }
                }
            }
        }
    }
}