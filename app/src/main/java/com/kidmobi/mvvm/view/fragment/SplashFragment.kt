package com.kidmobi.mvvm.view.fragment

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.work.*
import com.google.firebase.auth.FirebaseAuth
import com.kidmobi.R
import com.kidmobi.assets.services.RemoteSettingsService
import com.kidmobi.assets.workers.RemoteSettingsWorker
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
        startSettingsService()
        //startSettingWorker()
        checkConnectivity()
    }

    private fun startSettingWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val settingsRequest =
            PeriodicWorkRequestBuilder<RemoteSettingsWorker>(1, TimeUnit.SECONDS)
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
                .build()

        context?.let { WorkManager.getInstance(it).enqueue(settingsRequest) }
    }

    private fun startSettingsService() {
        Intent(context, RemoteSettingsService::class.java).also {
            requireActivity().startService(it)
            Timber.d("Remote settings service is started in Splash Fragment")
        }
    }

    private fun checkConnectivity() {

        connectivityManager = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (isOnline()) {
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)

        } else {
            Timber.d("No internet connection!")
            showToast()
        }
    }

    private fun isOnline(): Boolean {
        val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        return networkInfo?.isConnected == true
    }

    private fun showToast() {
        Toast.makeText(
            context,
            getString(R.string.splash_internet_baglantisi_hatasi),
            Toast.LENGTH_LONG
        ).show()
    }
}