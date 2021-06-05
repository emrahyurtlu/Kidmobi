package com.kidmobi.business.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.google.firebase.firestore.FirebaseFirestore
import com.kidmobi.R
import com.kidmobi.business.utils.Constants.NOTIFICATION_CHANNEL_ID
import com.kidmobi.business.utils.Constants.NOTIFICATION_CHANNEL_NAME
import com.kidmobi.business.utils.Constants.NOTIFICATION_ID
import com.kidmobi.business.utils.SettingsUtil
import com.kidmobi.business.utils.SharedPrefsUtil
import com.kidmobi.business.utils.enums.DbCollection
import com.kidmobi.business.utils.extensions.modelExtensions.isNotNull
import com.kidmobi.business.utils.extensions.modelExtensions.isValid
import com.kidmobi.business.utils.extensions.toMobileDevice
import com.kidmobi.data.model.MobileDevice
import com.kidmobi.ui.view.MainActivity
import timber.log.Timber
import java.util.*

class RemoteService : LifecycleService() {
    lateinit var settingsUtil: SettingsUtil
    lateinit var sharedPrefsUtil: SharedPrefsUtil
    lateinit var db: FirebaseFirestore
    lateinit var device: MobileDevice
    lateinit var deviceId: String

    override fun onCreate() {
        super.onCreate()
        settingsUtil = SettingsUtil(this)
        sharedPrefsUtil = SharedPrefsUtil(this)
        db = FirebaseFirestore.getInstance()
        deviceId = sharedPrefsUtil.getDeviceId()
        Timber.d("Service is initiated.")
        Timber.d("DEVICE_ID: $deviceId")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        startForegroundService()

        changeSettings()

        return START_STICKY
    }

    private fun changeSettings() {
        db.collection(DbCollection.MobileDevices.name).document(deviceId).addSnapshotListener { snapshot, e ->
            if (e != null) {
                Timber.e(e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                //Timber.d("RemoteService current device data: ${snapshot.data}")
                device = snapshot.toMobileDevice()

                Timber.d("RemoteService current device isNotNull: ${device.isNotNull()}")
                Timber.d("RemoteService current device session isValid: ${device.session.isValid()}")

                if (device.isNotNull() && device.session.isValid()) {
                    Timber.d("Device settings are adjusting...")
                    settingsUtil.changeDeviceSound(device.settings.soundLevel.toInt())
                    settingsUtil.changeScreenBrightness(device.settings.brightnessLevel.toInt())
                }

            } else {
                Timber.d("Current data: null")
                Timber.e("Snapshot is null")
            }
        }
    }

    private fun startForegroundService() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(getString(R.string.kidmobi_is_running))
            .setContentIntent(getMainActivityPendingIntent())

        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this, MainActivity::class.java),
        FLAG_UPDATE_CURRENT
    )

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }
}