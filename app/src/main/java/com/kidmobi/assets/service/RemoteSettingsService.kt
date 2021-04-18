package com.kidmobi.assets.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.google.firebase.firestore.FirebaseFirestore
import com.kidmobi.assets.enums.DbCollection
import com.kidmobi.assets.repositories.MobileDeviceRepo
import com.kidmobi.assets.utils.SettingsUtil
import com.kidmobi.assets.utils.SharedPrefsUtil
import com.kidmobi.assets.utils.toMobileDevice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class RemoteSettingsService : Service() {

    lateinit var settingsUtil: SettingsUtil
    lateinit var sharedPrefsUtil: SharedPrefsUtil
    lateinit var repo: MobileDeviceRepo
    lateinit var db: FirebaseFirestore

    override fun onBind(intent: Intent?): IBinder? {
        Timber.d("onBind is started!")
        return null
    }

    override fun onCreate() {
        super.onCreate()
        settingsUtil = SettingsUtil(this)
        sharedPrefsUtil = SharedPrefsUtil(this)
        repo = MobileDeviceRepo(FirebaseFirestore.getInstance())
        db = FirebaseFirestore.getInstance()
        Timber.d("onCreate is started!")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("onStartCommand is started!")
        val deviceId = sharedPrefsUtil.getDeviceId()
        CoroutineScope(Dispatchers.Main).launch {
            //val device = repo.getById(deviceId)
            db.collection(DbCollection.MobileDevices.name).document(deviceId).addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Timber.w("Listen failed: ${e.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    Timber.d("Current data: ${snapshot.data}")
                    val device = snapshot.toMobileDevice()
                    Timber.d("$device")
                    settingsUtil.changeDeviceSound(device.settings.soundLevel.toInt())
                    settingsUtil.changeScreenBrightness(device.settings.brightnessLevel.toInt())
                } else {
                    Timber.d("Current data: null")
                }
            }

        }


        return START_STICKY
    }

}