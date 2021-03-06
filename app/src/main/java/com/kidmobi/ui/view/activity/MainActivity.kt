package com.kidmobi.ui.view.activity

import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.work.*
import com.kidmobi.R
import com.kidmobi.business.receivers.*
import com.kidmobi.business.services.RemoteService
import com.kidmobi.business.services.RunningProcessesService
import com.kidmobi.business.workers.RemoteSettingsWorker
import com.kidmobi.business.workers.RunningProcessesWorker
import com.kidmobi.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        registerAppRemovedReceiver()
        registerNewAppInstalledReceiver()
        registerRemoteSettingsReceiver()
        registerVolumeChangedReceiver()
        registerBatteryChangedReceiver()

        startSettingWorker()
        startRemoteService()

        startRunningProcessesWorker()
        startRunningProcessesService()

    }

    private fun registerAppRemovedReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED)
        intentFilter.also {
            registerReceiver(AppRemovedReceiver(), it)
        }
    }

    private fun registerNewAppInstalledReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_INSTALL_PACKAGE)
        intentFilter.also {
            registerReceiver(NewAppInstalledReceiver(), it)
        }
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


    private fun startRunningProcessesWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val settingsRequest =
            PeriodicWorkRequestBuilder<RunningProcessesWorker>(1, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
                .build()

        WorkManager.getInstance(this).enqueue(settingsRequest)
    }

    private fun startRunningProcessesService() =
        Intent(this, RunningProcessesService::class.java).also {
            ContextCompat.startForegroundService(this, it)
        }
}