package com.kidmobi.mvvm.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
import com.kidmobi.databinding.FragmentDashboardBinding
import com.kidmobi.mvvm.model.MobileDevice
import com.kidmobi.mvvm.view.LoginActivity
import com.kidmobi.mvvm.view.MobileDeviceActivity
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

    //private lateinit var fManager: FragmentManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.top_app_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.signOut) signOut(item)
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPrefsUtil.setDeviceId()

        setUpTabs()

        setHasOptionsMenu(true)

        MobileAds.initialize(context)
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

    private fun setUpTabs() {
        val adapter = DashboardViewPager2Adapter(parentFragmentManager, lifecycle)

        adapter.addFragment(MobileDevicesFragment(), getString(R.string.dashboard_tab1_txt))
        adapter.addFragment(DeviceIdentityFragment(), getString(R.string.dashboard_tab2_txt))

        binding.viewPager2.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = adapter.getTitle(position)
        }.attach()

    }

    fun signOut(item: MenuItem) {
        auth.signOut()
        activity?.goto(LoginActivity::class.java, true)
    }

    fun addNewDeviceFab(view: View) {
        val integrator = IntentIntegrator(activity).apply {
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

                val intent = Intent(context, MobileDeviceActivity::class.java)
                intent.putExtra("device", device)
                startActivity(intent)

            } else {
                Toast.makeText(
                    context,
                    getString(R.string.qr_couldnt_read),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }
}