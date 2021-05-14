package com.kidmobi.business.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.kidmobi.business.services.RemoteService
import timber.log.Timber

class RemoteSettingsServiceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            when (it.action) {
                Intent.ACTION_BOOT_COMPLETED -> startService(context)
                Intent.ACTION_REBOOT -> startService(context)
            }
        }
    }

    private fun startService(context: Context?) {
        try {
            Intent(context, RemoteService::class.java).also {
                ContextCompat.startForegroundService(context!!, it)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}