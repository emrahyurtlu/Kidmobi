package com.kidmobi.assets.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.google.firebase.firestore.FirebaseFirestore
import com.kidmobi.assets.enums.DbCollection
import com.kidmobi.assets.utils.SettingsUtil
import com.kidmobi.assets.utils.SharedPrefsUtil
import com.kidmobi.assets.utils.extensions.modelExtensions.isValid
import com.kidmobi.assets.utils.extensions.toDeviceSession
import com.kidmobi.assets.utils.extensions.toMobileDevice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class RemoteSettingsService : Service() {

    lateinit var settingsUtil: SettingsUtil
    lateinit var sharedPrefsUtil: SharedPrefsUtil
    lateinit var db: FirebaseFirestore

    override fun onBind(intent: Intent?): IBinder? {
        Timber.d("onBind is started!")
        return null
    }

    override fun onCreate() {
        super.onCreate()
        settingsUtil = SettingsUtil(this)
        sharedPrefsUtil = SharedPrefsUtil(this)
        db = FirebaseFirestore.getInstance()
        Timber.d("onCreate is started!")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("onStartCommand is started!")
        val deviceId = sharedPrefsUtil.getDeviceId()
        CoroutineScope(Dispatchers.Main).launch {
            //Check whether session is started!
            db.collection(DbCollection.DeviceSessions.name)
                .whereEqualTo("sessionOwnerDeviceId", deviceId)
                .whereEqualTo("done", false)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Timber.w("There is no opened session: ${e.message}")
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.documents.size > 0) {
                        Timber.d("Current session data: ${snapshot.documents.first().data}")
                        val session = snapshot.documents.first().toDeviceSession()
                        if (session.isValid())
                            changeSettings(deviceId)
                    }
                }
        }


        return START_STICKY
    }

    private fun changeSettings(deviceId: String) {
        db.collection(DbCollection.MobileDevices.name).document(deviceId).addSnapshotListener { snapshot, e ->
            if (e != null) {
                Timber.w("Listen failed: ${e.message}")
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Timber.d("Current device data: ${snapshot.data}")
                val device = snapshot.toMobileDevice()
                //Timber.d("$device")
                settingsUtil.changeDeviceSound(device.settings.soundLevel.toInt())
                settingsUtil.changeScreenBrightness(device.settings.brightnessLevel.toInt())
            } else {
                Timber.d("Current data: null")
            }
        }
    }

}