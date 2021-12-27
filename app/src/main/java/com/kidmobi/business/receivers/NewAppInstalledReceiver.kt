package com.kidmobi.business.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.firebase.firestore.FirebaseFirestore
import com.kidmobi.business.utils.enums.DbCollection
import com.kidmobi.business.utils.extensions.toMobileDevice
import com.kidmobi.business.utils.misc.InstalledAppsUtil
import com.kidmobi.business.utils.misc.SharedPrefsUtil
import com.kidmobi.data.model.MobileDevice
import timber.log.Timber

class NewAppInstalledReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            when (it.action) {
                Intent.ACTION_INSTALL_PACKAGE -> updateInstalledAppList(context)
            }
        }
    }

    private fun updateInstalledAppList(context: Context?) {
        Timber.d("New App is installed on device.")
        try {
            context?.let { ctx ->
                // Get Device ID
                val sharedPreferences = SharedPrefsUtil(ctx.applicationContext)
                val deviceId = sharedPreferences.getDeviceId()

                // Get App List
                val appListUtil = InstalledAppsUtil(ctx.applicationContext)
                val list = appListUtil.getList()

                // Get Mobile Device and Set Device List
                val db: FirebaseFirestore = FirebaseFirestore.getInstance()
                var device = MobileDevice()
                db.collection(DbCollection.ManagedDevices.name).document(deviceId).get().addOnSuccessListener {
                    if (it.exists()) {
                        device = it.toMobileDevice()
                    }
                }
                device.apps = list

                // Save MobileDevice
                db.collection(DbCollection.ManagedDevices.name).document(deviceId).set(device)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}