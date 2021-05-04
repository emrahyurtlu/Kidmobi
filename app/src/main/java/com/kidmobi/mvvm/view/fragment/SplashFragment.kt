package com.kidmobi.mvvm.view.fragment

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.work.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.kidmobi.R
import com.kidmobi.business.receivers.RemoteSettingsServiceBroadcastReceiver
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
        registerRemoteSettingsReceiver()
        startSettingWorker()
        startRemoteService()
        checkConnectivity()
    }

    private fun registerRemoteSettingsReceiver() {
        IntentFilter(Intent.ACTION_BOOT_COMPLETED).also {
            requireActivity().registerReceiver(RemoteSettingsServiceBroadcastReceiver(), it)
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
            requireContext().startService(it)
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