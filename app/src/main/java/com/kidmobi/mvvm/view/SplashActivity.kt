package com.kidmobi.mvvm.view

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.google.firebase.auth.FirebaseAuth
import com.kidmobi.R
import com.kidmobi.assets.service.SettingsService
import com.kidmobi.assets.utils.goto
import com.kidmobi.assets.workers.SettingsWorker
import com.kidmobi.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val TAG = "SplashActivity"

    @Inject
    lateinit var auth: FirebaseAuth
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //auth = FirebaseAuth.getInstance()

        //startSettingsService()
        //startSettingWorker()

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
        val intent = Intent(applicationContext, SettingsService::class.java)
        startService(intent)
    }

    private fun checkConnectivity() {

        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (isOnline()) {

            /*auth.currentUser?.let { user ->
                Log.d(TAG, "checkConnectivity: ${user.providerData}")
                Log.d(TAG, "checkConnectivity: ${user.providerId}")

                val target:Class<*> = if (!user.isAnonymous) {
                    DashboardActivity::class.java
                } else {
                    LoginActivity::class.java
                }
                this.goto(target)
                finish()
            }*/
            this.goto(LoginActivity::class.java)
            finish()

        } else {
            Log.d(TAG, "checkConnectivity: No internet connection!")
            showToast()
        }
    }

    private fun isOnline(): Boolean {
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connMgr.activeNetworkInfo
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