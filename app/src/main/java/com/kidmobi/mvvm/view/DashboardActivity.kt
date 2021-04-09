package com.kidmobi.mvvm.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.zxing.integration.android.IntentIntegrator
import com.kidmobi.R
import com.kidmobi.assets.adapter.DashboardViewPager2Adapter
import com.kidmobi.assets.utils.SharedPrefsUtil
import com.kidmobi.databinding.ActivityDashboardBinding
import com.kidmobi.mvvm.model.MobileDevice
import com.kidmobi.mvvm.view.fragment.DeviceIdentityFragment
import com.kidmobi.mvvm.view.fragment.MobileDevicesFragment
import com.kidmobi.mvvm.viewmodel.MobileDeviceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    private val mobileDeviceViewModel: MobileDeviceViewModel by viewModels()
    private val TAG = "DashboardActivity"
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val sharedPrefsUtil = SharedPrefsUtil(applicationContext)
        sharedPrefsUtil.setDeviceId()

        saveDevice(sharedPrefsUtil.getDeviceId())
        setUpTabs()

        MobileAds.initialize(this)
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        checkSystemSettingsChangable()

        // Banner: Dashboard bottom banner
        // ca-app-pub-9250940245734350/3048347271

        // Interstitial Dashboard to SettingsActiviyt
        // ca-app-pub-9250940245734350/1237955040

        /*
        * TEST ID
        *   Banner: ca-app-pub-3940256099942544/6300978111
        *   Interstitial: ca-app-pub-3940256099942544/1033173712
        *
        * */

    }

    private fun saveDevice(uniqueDeviceId: String) {
        CoroutineScope(Dispatchers.Default).launch {
            mobileDeviceViewModel.saveDeviceInitially(uniqueDeviceId)
        }
    }

    private fun checkSystemSettingsChangable() {
        if (!Settings.System.canWrite(this)) {
            MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.permission_of_changing_system_settings))
                .setMessage(getString(R.string.permission_of_system_setting_msg))
                .setCancelable(false)
                .setNegativeButton(R.string.no) { dialog, which ->
                    dialog.cancel()
                    Toast.makeText(
                        findViewById(android.R.id.content),
                        getString(R.string.no_permission_msg),
                        Toast.LENGTH_LONG
                    ).show()
                    exitProcess(-1)
                }
                .setPositiveButton(R.string.yes) { dialog, which ->
                    getDeviceSettingChangePermission()
                    dialog.dismiss()
                    finish()
                }
                .show()
        }
    }

    private fun getDeviceSettingChangePermission() {
        if (!Settings.System.canWrite(this)) {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }


    private fun setUpTabs() {
        val adapter = DashboardViewPager2Adapter(supportFragmentManager, lifecycle)

        adapter.addFragment(MobileDevicesFragment(), getString(R.string.dashboard_tab1_txt))
        adapter.addFragment(DeviceIdentityFragment(), getString(R.string.dashboard_tab2_txt))

        binding.viewPager2.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = adapter.getTitle(position)
        }.attach()

    }

    fun signOut(item: MenuItem) {
        val auth = FirebaseAuth.getInstance()
        auth.signOut()
        val gotoLogin = Intent(this, LoginActivity::class.java)
        startActivity(gotoLogin)
        finish()
    }

    fun addNewDeviceFab(view: View) {
        val integrator = IntentIntegrator(this).apply {
            setBeepEnabled(false)
            setOrientationLocked(false)
            setPrompt(getString(R.string.cihaz_ekleniyor))
            setBarcodeImageEnabled(false)
            captureActivity = QrCaptureActivity::class.java
        }
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null && result.contents != null) {

                val device = MobileDevice(deviceId = result.contents)
                val intent = Intent(this, MobileDeviceActivity::class.java)

                intent.putExtra("device", device)
                startActivity(intent)
                // save device
            } else {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.qr_couldnt_read),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")
    }
}