package com.kidmobi.business.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import timber.log.Timber

class VolumeChangedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            AudioManager.ACTION_SCO_AUDIO_STATE_UPDATED -> {
                Timber.d("ACTION_SCO_AUDIO_STATE_UPDATED")
            }
            Intent.ACTION_MEDIA_BUTTON -> {
                Timber.d("ACTION_MEDIA_BUTTON")
            }
            Intent.ACTION_CONFIGURATION_CHANGED -> {
                Timber.d("ACTION_CONFIGURATION_CHANGED")
            }
            Intent.ACTION_MAIN -> {
                Timber.d("ACTION_MAIN")
            }
        }
    }
}