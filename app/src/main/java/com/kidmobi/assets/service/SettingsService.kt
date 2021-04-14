package com.kidmobi.assets.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.kidmobi.R
import com.kidmobi.assets.enums.IntentConstants
import com.kidmobi.assets.utils.SettingsUtil
import com.kidmobi.mvvm.model.MobileDevice
import com.kidmobi.mvvm.view.SplashActivity
import timber.log.Timber

class SettingsService : Service() {
    private val CHANNEL_ID = "KIDMOBI SERVICE"
    //private var settingsViewModel: SettingsViewModel = SettingsViewModel()
    //lateinit var auth: FirebaseAuth

    lateinit var settingsUtil: SettingsUtil
    //lateinit var sharedPrefsUtil: SharedPrefsUtil

    lateinit var notificationManager: NotificationManager

    override fun onBind(intent: Intent?): IBinder? {
        Timber.d("onBind: ")
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Timber.d("onCreate: ")
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        //auth = FirebaseAuth.getInstance()
        //sharedPrefsUtil = SharedPrefsUtil(this)
        settingsUtil = SettingsUtil(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("onDestroy: ")
        notificationManager.cancel(R.string.settings_service_desc)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var device: MobileDevice? = null
        Timber.d("onStartCommand: ")

        if (intent != null) {
            if (intent.hasExtra(IntentConstants.Device.name))
                device = intent.getSerializableExtra(IntentConstants.Device.name) as MobileDevice
        }


        val pendingIntent: PendingIntent =
            Intent(this, SplashActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name))
            .setContentText("KidMobi çalışıyor...")
            .setSmallIcon(R.drawable.ic_logo)
            .setContentIntent(pendingIntent)
            .setTicker("KidMobi çalışıyor...")
            .build()


        device?.let {
            Timber.d("Device settings are ready to adjust: $it")
            settingsUtil.changeDeviceSound(it.settings.soundLevel.toInt())
            settingsUtil.changeScreenBrightness(it.settings.brightnessLevel.toInt())
        }

        startForeground(1, notification)
        return START_STICKY
    }
}


