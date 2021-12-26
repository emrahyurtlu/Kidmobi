package com.kidmobi.ui.view.fragment

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.kidmobi.R
import com.kidmobi.business.utils.extensions.setMaterialToolbar
import com.kidmobi.data.model.MobileDevice
import com.kidmobi.databinding.FragmentDeviceManagementBinding
import com.kidmobi.ui.view.adapter.DeviceManagementViewPagerAdapter
import com.kidmobi.ui.view.fragment.tabs.devicemanagement.DeviceManagementGeneralTabFragment
import com.kidmobi.ui.view.fragment.tabs.devicemanagement.DeviceManagementInstalledAppsTabFragment
import com.kidmobi.ui.viewmodel.ManagedDevicesViewModel
import com.kidmobi.ui.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class DeviceManagementFragment : Fragment() {
    @Inject
    lateinit var device: MobileDevice
    private lateinit var binding: FragmentDeviceManagementBinding
    private val settingsViewModel: SettingsViewModel by viewModels()
    private val managedDevicesViewModel: ManagedDevicesViewModel by viewModels()
    private val args: DeviceManagementFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.settings_top_app_bar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_device_management, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.setMaterialToolbar(binding.topAppBar, R.id.action_deviceManagementFragment_to_dashboardFragment)

        device = args.device

        binding.managedDevicesViewModel = managedDevicesViewModel
        binding.settingsViewModel = settingsViewModel

        binding.topAppBar.let {
            it.inflateMenu(R.menu.settings_top_app_bar)
            it.setOnMenuItemClickListener { mi ->
                optionsItemSelected(mi)
            }
            it.title = device.deviceOwnerName
        }

        binding.mobileDevice = device

        setUpTabs()
    }

    /*private fun setUpAds() {
        MobileAds.initialize(requireContext())
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }*/

    private fun setUpTabs() {
        val adapter = DeviceManagementViewPagerAdapter(parentFragmentManager, lifecycle)

        adapter.addFragment(DeviceManagementGeneralTabFragment(device), getString(R.string.device_man_tab_general))
        adapter.addFragment(DeviceManagementInstalledAppsTabFragment(), getString(R.string.device_man_tab_apps))

        binding.viewPager2.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = adapter.getTitle(position)
        }.attach()

    }

    private fun optionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.miDeleteMobileDevice -> deleteMobileDevice()
        }
        return true
    }

    private fun deleteMobileDevice() {

        Timber.d("This device will be deleted: $device")

        MaterialAlertDialogBuilder(requireActivity())
            .setTitle(device.deviceOwnerName)
            .setMessage(getString(R.string.are_you_sure_to_delete))
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                managedDevicesViewModel.deleteFromMyDevices(device.deviceId)
                dialog.dismiss()
                findNavController().navigate(R.id.action_deviceManagementFragment_to_dashboardFragment)
            }
            .show()
    }
}