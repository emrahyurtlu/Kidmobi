package com.kidmobi.ui.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.zxing.integration.android.IntentIntegrator
import com.kidmobi.R
import com.kidmobi.business.utils.misc.SharedPrefsUtil
import com.kidmobi.business.utils.extensions.checkSystemSettingsAdjustable
import com.kidmobi.business.utils.extensions.modelExtensions.isNotNull
import com.kidmobi.data.model.MobileDevice
import com.kidmobi.databinding.FragmentDashboardBinding
import com.kidmobi.ui.view.QrCaptureActivity
import com.kidmobi.ui.view.adapter.DashboardViewPager2Adapter
import com.kidmobi.ui.view.fragment.tabs.dashboard.DeviceIdentityTabFragment
import com.kidmobi.ui.view.fragment.tabs.dashboard.MobileDevicesTabFragment
import com.kidmobi.ui.viewmodel.MobileDeviceViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class DashboardFragment : Fragment() {
    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var sharedPrefsUtil: SharedPrefsUtil

    private val viewModel: MobileDeviceViewModel by viewModels()

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

        this.checkSystemSettingsAdjustable()

        binding.btnFab.setOnClickListener { addNewDeviceFab() }
    }

    /*private fun setUpAds() {
        MobileAds.initialize(requireContext())
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }*/

    private fun setUpTabs() {
        val adapter = DashboardViewPager2Adapter(parentFragmentManager, lifecycle)

        adapter.addFragment(MobileDevicesTabFragment(), getString(R.string.dashboard_tab1_txt))
        adapter.addFragment(DeviceIdentityTabFragment(), getString(R.string.dashboard_tab2_txt))

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
        Timber.d("Qr reading is started!")
        val integrator = IntentIntegrator.forSupportFragment(this).apply {
            setBeepEnabled(false)
            setOrientationLocked(false)
            setPrompt(getString(R.string.cihaz_ekleniyor))
            setBarcodeImageEnabled(false)
            captureActivity = QrCaptureActivity::class.java
            setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        }
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null && result.contents != null) {
                val deviceId = result.contents

                viewModel.getDeviceById(deviceId)
                viewModel.device.observe(viewLifecycleOwner, { device ->
                    Timber.d("New device id: $deviceId")
                    Timber.d("New device is: $device")

                    if (device.isNotNull())
                        findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToAddMobileDeviceFragment(device))
                    else
                        findNavController().navigate(DashboardFragmentDirections.actionDashboardFragmentToAddMobileDeviceFragment(MobileDevice(deviceId)))

                })


            } else {
                Timber.d(getString(R.string.qr_couldnt_read))
                Snackbar.make(
                    requireView(),
                    getString(R.string.qr_couldnt_read),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

    }
}