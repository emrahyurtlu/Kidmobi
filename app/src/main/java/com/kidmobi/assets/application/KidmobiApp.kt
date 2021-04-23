package com.kidmobi.assets.application

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kidmobi.assets.enums.DbCollection
import com.kidmobi.assets.enums.UserType
import com.kidmobi.assets.utils.SharedPrefsUtil
import com.kidmobi.assets.utils.extensions.modelExtensions.init
import com.kidmobi.assets.utils.printsln
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
        Timber.plant(Timber.DebugTree())
        saveDevice(sharedPrefsUtil.getDeviceId())
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "KidMobi Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun saveDevice(uniqueDeviceId: String) {
        printsln("KidmobiApp::saveDevice => $uniqueDeviceId")
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

    companion object {
        private const val CHANNEL_ID = "KIDMOBI SERVICE"
    }
}