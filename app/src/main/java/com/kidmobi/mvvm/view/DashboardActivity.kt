package com.kidmobi.mvvm.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.zxing.integration.android.IntentIntegrator
import com.kidmobi.R
import com.kidmobi.assets.adapter.DashboardViewPager2Adapter
import com.kidmobi.assets.utils.SharedPrefsUtil
import com.kidmobi.assets.utils.extensions.checkSystemSettingsAdjustable
import com.kidmobi.assets.utils.extensions.goto
import com.kidmobi.databinding.ActivityDashboardBinding
import com.kidmobi.mvvm.model.MobileDevice
import com.kidmobi.mvvm.view.fragment.DeviceIdentityFragment
import com.kidmobi.mvvm.view.fragment.MobileDevicesFragment
import com.kidmobi.mvvm.viewmodel.ManagedDevicesViewModel
import com.kidmobi.mvvm.viewmodel.MobileDeviceViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var device: MobileDevice

    @Inject
    lateinit var sharedPrefsUtil: SharedPrefsUtil
    private val mobileDeviceViewModel: MobileDeviceViewModel by viewModels()
    private val managedDevicesViewModel: ManagedDevicesViewModel by viewModels()
    private lateinit var binding: ActivityDashboardBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPrefsUtil.setDeviceId()

        setUpTabs()

        MobileAds.initialize(this)
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        this.checkSystemSettingsAdjustable()

        // Banner: Dashboard bottom banner
        // ca-app-pub-9250940245734350/3048347271

        // Interstitial Dashboard to SettingsActivity
        // ca-app-pub-9250940245734350/1237955040

        /*
        * TEST ID
        *   Banner: ca-app-pub-3940256099942544/6300978111
        *   Interstitial: ca-app-pub-3940256099942544/1033173712
        *
        * */
    }

    private fun saveDevice(uniqueDeviceId: String) {
        mobileDeviceViewModel.saveDeviceInitially(uniqueDeviceId)
        /*GlobalScope.launch()
        {
            try {
                runOnUiThread {
                    mobileDeviceViewModel.saveDeviceInitially(uniqueDeviceId)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }*/
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
        auth.signOut()
        this.goto(LoginActivity::class.java)
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
                val deviceId = result.contents
                device = mobileDeviceViewModel.getDeviceById(deviceId)

                Timber.asTree().d("New device is trying to add your collection: $device")

                val intent = Intent(this, MobileDeviceActivity::class.java)
                intent.putExtra("device", device)
                startActivity(intent)

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
        Timber.d("onStart: ")
    }
}