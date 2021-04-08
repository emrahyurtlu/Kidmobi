package com.kidmobi.assets.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.kidmobi.assets.utils.SettingsUtil
import com.kidmobi.assets.utils.SharedPrefsUtil
import com.kidmobi.mvvm.viewmodel.SettingsViewModel
import com.kidmobi.assets.utils.printsln

class SettingsWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    private var settingsViewModel: SettingsViewModel = SettingsViewModel()
    private var settingsUtil: SettingsUtil =
        SettingsUtil(applicationContext, applicationContext.contentResolver)
    private var auth = FirebaseAuth.getInstance()

    override fun doWork(): Result {
        printsln("SettingsWorker is running", TAG)
        auth.currentUser?.let {
            val sharedPrefsUtil = SharedPrefsUtil(applicationContext)
            val deviceId = sharedPrefsUtil.getDeviceId()
            settingsViewModel.getCurrentMobileDevice(deviceId)
            val thisDevice = settingsViewModel.currentDevice.value
            thisDevice?.let {
                settingsUtil.changeDeviceSound(it.settings.soundLevel.toInt())
                settingsUtil.changeScreenBrightness(it.settings.brightnessLevel.toInt())
            }
        }
        return Result.retry()
    }

    companion object {
        private const val TAG = "SettingsWorker"
    }
}