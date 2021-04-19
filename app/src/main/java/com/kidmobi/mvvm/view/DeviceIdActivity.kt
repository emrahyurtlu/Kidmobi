package com.kidmobi.mvvm.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.kidmobi.R
import com.kidmobi.assets.utils.checkSystemSettingsAdjustable
import com.kidmobi.databinding.ActivityDeviceIdBinding
import com.kidmobi.mvvm.view.fragment.DeviceIdentityFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeviceIdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeviceIdBinding
    private lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceIdBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.checkSystemSettingsAdjustable()
        MobileAds.initialize(this)
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.qrCodeFragment, DeviceIdentityFragment()).commit()
        // AdMob ID: ca-app-pub-9250940245734350/7637364948
    }

    fun turnBack(view: View) = finish()
}