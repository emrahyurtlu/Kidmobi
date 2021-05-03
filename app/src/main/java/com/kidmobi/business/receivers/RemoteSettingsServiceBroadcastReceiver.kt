package com.kidmobi.business.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.kidmobi.business.services.RemoteService

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
        Intent(context, RemoteService::class.java).also {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                context?.startForegroundService(it)
            else
                context?.startService(it)
        }
    }
}