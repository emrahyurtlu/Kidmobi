package com.kidmobi.business.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.kidmobi.business.services.RemoteService
import timber.log.Timber

class BatteryOkReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            when (it.action) {
                Intent.ACTION_BATTERY_OKAY -> startService(context)
            }
        }
    }

    private fun startService(context: Context?) {
        Timber.d("RemoteService is started via BatteryOkReceiver")
        try {
            Intent(context, RemoteService::class.java).also {
                ContextCompat.startForegroundService(context!!, it)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}