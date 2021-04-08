package com.kidmobi.mvvm.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import com.kidmobi.R
import com.kidmobi.databinding.ActivityDeviceIdBinding
import com.kidmobi.assets.utils.printsln
import com.mobicon.android.mvvm.view.fragment.DeviceIdentityFragment

class DeviceIdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeviceIdBinding
    private lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceIdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this)
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.qrCodeFragment, DeviceIdentityFragment()).commit()
        // AdMob ID: ca-app-pub-9250940245734350/7637364948
        val auth = FirebaseAuth.getInstance()
        val authUserInfo = auth.currentUser?.providerData
        if (authUserInfo != null) {
            for (p in authUserInfo) {
                printsln(p.providerId, "providerId")
                printsln(p.email, "email")
                printsln(p.phoneNumber, "phoneNumber")
            }
        }
    }

    fun turnBack(view: View) = finish()
}