package com.kidmobi.ui.view.fragment

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.work.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.kidmobi.R
import com.kidmobi.business.receivers.BatteryChangedReceiver
import com.kidmobi.business.receivers.RemoteSettingsServiceReceiver
import com.kidmobi.business.receivers.VolumeChangedReceiver
import com.kidmobi.business.services.RemoteService
import com.kidmobi.business.workers.RemoteSettingsWorker
import com.kidmobi.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding

    @Inject
    lateinit var auth: FirebaseAuth
    private lateinit var connectivityManager: ConnectivityManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*registerRemoteSettingsReceiver()
        registerVolumeChangedReceiver()
        registerBatteryChangedReceiver()*/

        /*startSettingWorker()
        startRemoteService()*/
        checkConnectivity()
    }

    private fun registerBatteryChangedReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED)
        intentFilter.also {
            requireActivity().registerReceiver(BatteryChangedReceiver(), it)
        }
    }

    private fun registerRemoteSettingsReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_BOOT_COMPLETED)
        intentFilter.addAction(Intent.ACTION_REBOOT)
        intentFilter.also {
            requireActivity().registerReceiver(RemoteSettingsServiceReceiver(), it)
        }
    }

    private fun registerVolumeChangedReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(AudioManager.ACTION_SCO_AUDIO_STATE_UPDATED)
        intentFilter.addAction(Intent.ACTION_MEDIA_BUTTON)
        intentFilter.addAction(Intent.ACTION_CONFIGURATION_CHANGED)
        intentFilter.addAction(Intent.ACTION_MAIN)
        intentFilter.also {
            requireActivity().registerReceiver(VolumeChangedReceiver(), it)
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

        WorkManager.getInstance(requireContext()).enqueue(settingsRequest)
    }

    private fun startRemoteService() =
        Intent(requireContext(), RemoteService::class.java).also {
            ContextCompat.startForegroundService(requireContext(), it)
        }

    private fun checkConnectivity() {

        connectivityManager = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (isOnline()) {
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        } else {
            Timber.d("No internet connection!")
            Snackbar.make(
                requireView(),
                getString(R.string.splash_internet_baglantisi_hatasi),
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun isOnline(): Boolean {
        val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        return networkInfo?.isConnected == true
    }
}