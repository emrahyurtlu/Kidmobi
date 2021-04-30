package com.kidmobi.assets.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import com.google.firebase.firestore.FirebaseFirestore
import com.kidmobi.R
import com.kidmobi.assets.enums.DbCollection
import com.kidmobi.assets.utils.SettingsUtil
import com.kidmobi.assets.utils.SharedPrefsUtil
import com.kidmobi.assets.utils.extensions.modelExtensions.isValid
import com.kidmobi.assets.utils.extensions.toDeviceSession
import com.kidmobi.assets.utils.extensions.toMobileDevice
import com.kidmobi.mvvm.view.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class RemoteSettingsService : Service() {

    lateinit var settingsUtil: SettingsUtil
    lateinit var sharedPrefsUtil: SharedPrefsUtil
    lateinit var db: FirebaseFirestore

    private var wakeLock: PowerManager.WakeLock? = null
    private var isServiceStarted = false

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
        val notification = createNotification()
        startForeground(1, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("onStartCommand is started!")
        val deviceId = sharedPrefsUtil.getDeviceId()
        CoroutineScope(Dispatchers.Main).launch {
            //Check whether session is started!
            startService()

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

    private fun startService() {
        if (isServiceStarted) return
        Timber.d("Starting the foreground service task")
        isServiceStarted = true

        // we need this lock so our service gets not affected by Doze Mode
        wakeLock =
            (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "EndlessService::lock").apply {
                    acquire()
                }
            }
    }


    private fun createNotification(): Notification {
        val notificationChannelId = getString(R.string.kidmobi_service_channel)

        // depending on the Android API that we're dealing with we will have
        // to use a specific method to create the notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                notificationChannelId,
                getString(R.string.kidmobi_is_running),
                NotificationManager.IMPORTANCE_HIGH
            ).let {
                it.description = getString(R.string.kidmobi_service_channel)
                it.enableLights(true)
                it.lightColor = Color.RED
                it
            }
            notificationManager.createNotificationChannel(channel)
        }

        val pendingIntent: PendingIntent = Intent(this, MainActivity::class.java).let { notificationIntent ->
            PendingIntent.getActivity(this, 0, notificationIntent, 0)
        }

        val builder: Notification.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Notification.Builder(
            this,
            notificationChannelId
        ) else Notification.Builder(this)


        return builder
            .setContentTitle(getString(R.string.foreground_service_title))
            .setContentText(getString(R.string.kidmobi_is_running))
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setTicker("Ticker text")
            .setPriority(Notification.PRIORITY_HIGH) // for under android 26 compatibility
            .build()
    }

}