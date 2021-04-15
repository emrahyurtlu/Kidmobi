package com.kidmobi.assets.application

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.kidmobi.assets.utils.SharedPrefsUtil
import com.kidmobi.mvvm.viewmodel.MobileDeviceViewModel
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltAndroidApp
class KidMobiApp : Application() {

    @Inject
    lateinit var sharedPrefsUtil: SharedPrefsUtil
    private val mobileDeviceViewModel: MobileDeviceViewModel = MobileDeviceViewModel()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        saveDevice(sharedPrefsUtil.getDeviceId())
        createNotificationChannel()
    }

    private fun saveDevice(uniqueDeviceId: String) {
        GlobalScope.launch()
        {
            try {
                Timber.d("Saving device")
                mobileDeviceViewModel.saveDeviceInitially(uniqueDeviceId)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
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

    companion object {
        private const val CHANNEL_ID = "KIDMOBI SERVICE"
    }
}