package com.kidmobi.ui.view

import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.work.*
import com.kidmobi.R
import com.kidmobi.business.receivers.BatteryChangedReceiver
import com.kidmobi.business.receivers.RemoteSettingsServiceReceiver
import com.kidmobi.business.receivers.VolumeChangedReceiver
import com.kidmobi.business.services.RemoteService
import com.kidmobi.business.workers.RemoteSettingsWorker
import com.kidmobi.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        //setContentView(R.layout.activity_main)

        registerRemoteSettingsReceiver()
        registerVolumeChangedReceiver()
        registerBatteryChangedReceiver()

        startSettingWorker()
        startRemoteService()

    }

    private fun registerBatteryChangedReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED)
        intentFilter.also {
            registerReceiver(BatteryChangedReceiver(), it)
        }
    }

    private fun registerRemoteSettingsReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_BOOT_COMPLETED)
        intentFilter.addAction(Intent.ACTION_REBOOT)
        intentFilter.also {
            registerReceiver(RemoteSettingsServiceReceiver(), it)
        }
    }

    private fun registerVolumeChangedReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(AudioManager.ACTION_SCO_AUDIO_STATE_UPDATED)
        intentFilter.addAction(Intent.ACTION_MEDIA_BUTTON)
        intentFilter.addAction(Intent.ACTION_CONFIGURATION_CHANGED)
        intentFilter.addAction(Intent.ACTION_MAIN)
        intentFilter.also {
            registerReceiver(VolumeChangedReceiver(), it)
        }
    }

    private fun startSettingWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val settingsRequest =
            PeriodicWorkRequestBuilder<RemoteSettingsWorker>(1, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
                .build()

        WorkManager.getInstance(this).enqueue(settingsRequest)
    }

    private fun startRemoteService() =
        Intent(this, RemoteService::class.java).also {
            ContextCompat.startForegroundService(this, it)
        }
}