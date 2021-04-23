package com.kidmobi.mvvm.view

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.google.firebase.auth.FirebaseAuth
import com.kidmobi.R
import com.kidmobi.assets.service.RemoteSettingsService
import com.kidmobi.assets.utils.extensions.goto
import com.kidmobi.assets.workers.SettingsWorker
import com.kidmobi.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    @Inject
    lateinit var auth: FirebaseAuth
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //startSettingsBackgroundService()
        startSettingsService()
        //startSettingWorker()
    }

    override fun onStart() {
        super.onStart()
        checkConnectivity()
    }

    private fun startSettingWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            //.setRequiresCharging(true)
            .build()
        val settingsRequest =
            PeriodicWorkRequestBuilder<SettingsWorker>(10, TimeUnit.MILLISECONDS)
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.MILLISECONDS)
                .addTag("SettingsWorker-SplashActivity")
                .build()

        WorkManager.getInstance(applicationContext).enqueue(settingsRequest)
    }

    private fun startSettingsService() {
        val intent = Intent(this, RemoteSettingsService::class.java)
        startService(intent)
    }

    private fun checkConnectivity() {

        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (isOnline()) {
            this.goto(LoginActivity::class.java)
            finish()

        } else {
            Timber.d("checkConnectivity: No internet connection!")
            showToast()
        }
    }

    private fun isOnline(): Boolean {
        val networkCallback = ConnectivityManager.NetworkCallback()
        val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        return networkInfo?.isConnected == true
    }

    private fun showToast() {
        Toast.makeText(
            this,
            getString(R.string.splash_internet_baglantisi_hatasi),
            Toast.LENGTH_LONG
        ).show()
    }
}