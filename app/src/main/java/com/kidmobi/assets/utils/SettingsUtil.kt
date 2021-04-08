package com.kidmobi.assets.utils

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.provider.Settings

class SettingsUtil(
    private var context: Context,
    private var contentResolver: ContentResolver
) {

    fun changeScreenBrightness(value: Int) {
        if (Settings.System.canWrite(context)) {
            Settings.System.putInt(
                contentResolver,
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
            )
            Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, value)
        } else {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    fun changeDeviceSound(value: Int) {
        val audioManager: AudioManager =
            context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        if (Settings.System.canWrite(context)) {

            audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC, value,
                AudioManager.FLAG_VIBRATE
            )
            audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                value,
                AudioManager.FLAG_VIBRATE
            )
            audioManager.setStreamVolume(
                AudioManager.STREAM_NOTIFICATION,
                value,
                AudioManager.FLAG_VIBRATE
            )
            audioManager.setStreamVolume(
                AudioManager.STREAM_RING,
                value,
                AudioManager.FLAG_VIBRATE
            )
            audioManager.setStreamVolume(
                AudioManager.STREAM_SYSTEM,
                value,
                AudioManager.FLAG_VIBRATE
            )
        } else {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}