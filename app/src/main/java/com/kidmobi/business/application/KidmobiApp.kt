package com.kidmobi.business.application

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kidmobi.business.enums.DbCollection
import com.kidmobi.business.enums.UserType
import com.kidmobi.business.utils.SharedPrefsUtil
import com.kidmobi.business.utils.extensions.modelExtensions.init
import com.kidmobi.mvvm.model.MobileDevice
import com.kidmobi.mvvm.model.MobileDeviceInfo
import com.kidmobi.mvvm.model.MobileDeviceSettings
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
        Timber.plant(object : Timber.DebugTree() {
            override fun createStackElementTag(element: StackTraceElement): String? {
                return String.format("%s::%s", element.className, element.methodName)
            }
        })
        saveDevice(sharedPrefsUtil.getDeviceId())
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
                    info = MobileDeviceInfo().init()
                    settings = MobileDeviceSettings().init()
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
                docRef.addSnapshotListener { snapshot, e ->
                    if (e == null) {
                        if (snapshot == null || !snapshot.exists()) {
                            docRef.set(device)
                            Timber.d("Current device is saved!")
                        }
                    }
                }
            }
        }
    }
}