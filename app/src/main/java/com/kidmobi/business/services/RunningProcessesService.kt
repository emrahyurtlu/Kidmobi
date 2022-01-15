package com.kidmobi.business.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.google.firebase.firestore.FirebaseFirestore
import com.kidmobi.R
import com.kidmobi.business.utils.constants.NotificationConstants
import com.kidmobi.business.utils.enums.DbCollection
import com.kidmobi.business.utils.extensions.modelExtensions.isNotNull
import com.kidmobi.business.utils.extensions.toMobileDevice
import com.kidmobi.business.utils.misc.RunningAppsUtil
import com.kidmobi.business.utils.misc.SharedPrefsUtil
import com.kidmobi.data.model.MobileDevice
import com.kidmobi.ui.view.activity.MainActivity
import timber.log.Timber


class RunningProcessesService : LifecycleService() {
    lateinit var appUtil: RunningAppsUtil
    lateinit var sharedPrefsUtil: SharedPrefsUtil
    lateinit var db: FirebaseFirestore
    lateinit var device: MobileDevice
    lateinit var deviceId: String

    override fun onCreate() {
        super.onCreate()
        appUtil = RunningAppsUtil(baseContext)
        sharedPrefsUtil = SharedPrefsUtil(this)
        db = FirebaseFirestore.getInstance()
        deviceId = sharedPrefsUtil.getDeviceId()
        Timber.d("RunningProcessesService is initiated.")
        Timber.d("DEVICE_ID: $deviceId")
        println("RunningProcessesService is started in onCreate().")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        startForegroundService()

        syncRunningProcesses()

        println("RunningProcessesService is started in onStartCommand()")

        return START_STICKY
    }

    private fun syncRunningProcesses() {
        println("syncRunningProcesses() is invoked!")
        val documentReference = db.collection(DbCollection.MobileDevices.name).document(deviceId)
        documentReference.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Timber.e(e.message)
                println(e.message)
                return@addSnapshotListener
            }

            //println(snapshot)

            if (snapshot != null && snapshot.exists()) {
                device = snapshot.toMobileDevice()

                println(device)
                Timber.d("RunningProcessService current device isNotNull: ${device.isNotNull()}")

                if (device.isNotNull()) {
                    Timber.d("Gathering active processes...")
                    device.runningApps = appUtil.getList()
                    documentReference.set(device)
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

        val notificationBuilder = NotificationCompat.Builder(this, NotificationConstants.NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(getString(R.string.kidmobi_is_running))
            .setContentIntent(getMainActivityPendingIntent())

        startForeground(NotificationConstants.NOTIFICATION_ID, notificationBuilder.build())
        RemoteService.isRunning = true
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this, MainActivity::class.java),
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NotificationConstants.NOTIFICATION_CHANNEL_ID,
            NotificationConstants.NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        var isRunning: Boolean = false
    }
}