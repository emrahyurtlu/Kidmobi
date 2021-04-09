package com.kidmobi.assets.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.kidmobi.assets.utils.SettingsUtil
import com.kidmobi.assets.utils.SharedPrefsUtil
import com.kidmobi.mvvm.viewmodel.SettingsViewModel
import javax.inject.Inject

class SettingsService : Service() {
    private val TAG = "SettingsService"
    private var settingsViewModel: SettingsViewModel = SettingsViewModel()

    @Inject
    lateinit var sharedPrefsUtil: SharedPrefsUtil

    private var auth = FirebaseAuth.getInstance()

    override fun onBind(intent: Intent?): IBinder? {
        Log.d(TAG, "onBind: ")

        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: ")
        auth.currentUser.let {
            //val sharedPrefsUtil = SharedPrefsUtil(this)
            val deviceId = sharedPrefsUtil.getDeviceId()
            val settingsUtil = SettingsUtil(this)
            settingsViewModel.getCurrentMobileDevice(deviceId)
            val thisDevice = settingsViewModel.currentDevice.value
            thisDevice.let {
                if (it != null) {
                    settingsUtil.changeDeviceSound(it.settings?.soundLevel!!.toInt())
                    settingsUtil.changeScreenBrightness(it.settings?.brightnessLevel!!.toInt())
                }
            }
        }
        return START_STICKY
    }
}