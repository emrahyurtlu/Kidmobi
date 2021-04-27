package com.kidmobi.mvvm.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.zxing.integration.android.IntentIntegrator
import com.kidmobi.R
import com.kidmobi.assets.adapter.DashboardViewPager2Adapter
import com.kidmobi.assets.utils.SharedPrefsUtil
import com.kidmobi.assets.utils.extensions.checkSystemSettingsAdjustable
import com.kidmobi.databinding.FragmentDashboardBinding
import com.kidmobi.mvvm.model.MobileDevice
import com.kidmobi.mvvm.view.QrCaptureActivity
import com.kidmobi.mvvm.viewmodel.MobileDeviceViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class DashboardFragment : Fragment() {
    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var device: MobileDevice

    @Inject
    lateinit var sharedPrefsUtil: SharedPrefsUtil

    private val mobileDeviceViewModel: MobileDeviceViewModel by viewModels()

    private lateinit var binding: FragmentDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        return binding.root
    }

    private fun optionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.miSignOut -> signOut()
            R.id.miAboutUs -> Timber.d("About Us is clicked in option menu!!!")
        }
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPrefsUtil.setDeviceId()

        setUpTabs()

        binding.topAppBar.inflateMenu(R.menu.top_app_bar)

        binding.topAppBar.setOnMenuItemClickListener {
            optionsItemSelected(it)
        }

        MobileAds.initialize(requireContext())
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        this.checkSystemSettingsAdjustable()

        binding.btnFab.setOnClickListener { addNewDeviceFab() }

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

    private fun setUpTabs() {
        val adapter = DashboardViewPager2Adapter(parentFragmentManager, lifecycle)

        adapter.addFragment(MobileDevicesFragment(), getString(R.string.dashboard_tab1_txt))
        adapter.addFragment(DeviceIdentityFragment(), getString(R.string.dashboard_tab2_txt))

        binding.viewPager2.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = adapter.getTitle(position)
        }.attach()

    }

    private fun signOut() {
        auth.signOut().also {
            findNavController().navigate(R.id.action_dashboardFragment_to_loginFragment)
        }
    }

    private fun addNewDeviceFab() {
        val integrator = IntentIntegrator(activity).apply {
            setBeepEnabled(false)
            setOrientationLocked(false)
            setPrompt(getString(R.string.cihaz_ekleniyor))
            setBarcodeImageEnabled(false)
            captureActivity = QrCaptureActivity::class.java
            setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
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

                Timber.d("New device is trying to add your collection: $device")

                findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToAddMobileDeviceFragment(device))

            } else {
                Timber.d(getString(R.string.qr_couldnt_read))
                Toast.makeText(
                    context,
                    getString(R.string.qr_couldnt_read),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }
}