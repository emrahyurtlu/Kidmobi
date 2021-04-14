package com.kidmobi.assets.workers

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kidmobi.assets.enums.DbCollection
import com.kidmobi.assets.enums.IntentConstants
import com.kidmobi.assets.service.SettingsService
import com.kidmobi.assets.utils.SettingsUtil
import com.kidmobi.assets.utils.SharedPrefsUtil
import com.kidmobi.assets.utils.printsln
import com.kidmobi.assets.utils.toMobileDevice
import com.kidmobi.mvvm.model.MobileDevice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class SettingsWorker(var context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    lateinit var settingsUtil: SettingsUtil
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore

    override fun doWork(): Result {
        Timber.d("SettingsWorker is running")
        CoroutineScope(Dispatchers.Default).launch {
            try {
                db = FirebaseFirestore.getInstance()
                auth = FirebaseAuth.getInstance()
                settingsUtil = SettingsUtil(context)
                auth.currentUser?.let {
                    var device: MobileDevice? = null
                    val sharedPrefsUtil = SharedPrefsUtil(context)
                    val deviceId = sharedPrefsUtil.getDeviceId()

                    db.collection(DbCollection.MobileDevices.name).document(deviceId).get().addOnSuccessListener { ds ->
                        if (ds.exists()) {
                            device = ds.toMobileDevice()
                            Timber.d("$device")
                        }
                    }

                    device?.let { d ->
                        val intent = Intent(context, SettingsService::class.java)
                        intent.putExtra(IntentConstants.Device.name, d)

                        ContextCompat.startForegroundService(context, intent)

                        Timber.d("$d")
                        printsln(d)
                    }

                }
            } catch (e: Exception) {
                Timber.e("Settings Worker Exception: ${e.message}")

            }
        }

        return Result.retry()
    }
}