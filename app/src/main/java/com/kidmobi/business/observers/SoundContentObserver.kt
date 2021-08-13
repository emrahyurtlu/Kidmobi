package com.kidmobi.business.observers

import android.content.Context
import android.database.ContentObserver
import android.media.AudioManager
import android.os.Handler
import android.os.Looper


class SoundContentObserver(val context: Context?, val funcParam: () -> Unit) : ContentObserver(Handler(Looper.getMainLooper())) {
    private val audio = context?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private var previousVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC)

    override fun onChange(selfChange: Boolean) {
        super.onChange(selfChange)
        val currentVolume: Int = audio.getStreamVolume(AudioManager.STREAM_MUSIC)
        val delta = previousVolume - currentVolume
        if (delta > 0) {
            previousVolume = currentVolume
            funcParam()
            println("Volume Decreased: $previousVolume")
        } else if (delta < 0) {
            previousVolume = currentVolume
            funcParam()
            println("Volume Increased $previousVolume")
        }
    }
}