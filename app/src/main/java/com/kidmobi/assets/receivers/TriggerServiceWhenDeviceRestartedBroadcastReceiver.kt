package com.kidmobi.assets.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kidmobi.assets.services.RemoteService
import timber.log.Timber

class TriggerServiceWhenDeviceRestartedBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            when (it.action) {
                Intent.ACTION_BOOT_COMPLETED -> startService(context)
            }
        }
    }

    private fun startService(context: Context?) {
        Timber.d("RemoteService is triggered via a broadcast receiver.")
        Intent(context, RemoteService::class.java).also {
            context?.startService(it)
        }
    }
}