package com.kidmobi.assets.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class SaveDeviceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            when (intent.action) {
                Intent.ACTION_RUN -> Toast.makeText(context, "Device is launched first!", Toast.LENGTH_LONG).show()
                else -> Toast.makeText(context, "SaveDeviceBroadcastReceiver is triggered!", Toast.LENGTH_LONG).show()
            }
        }
        //Toast.makeText(context, "Device is launched first!", Toast.LENGTH_LONG).show()
    }
}