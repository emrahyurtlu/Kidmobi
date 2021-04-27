package com.kidmobi.mvvm.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.slider.Slider
import com.kidmobi.R
import com.kidmobi.databinding.FragmentDeviceManagementBinding
import com.kidmobi.mvvm.model.MobileDevice
import com.kidmobi.mvvm.viewmodel.ManagedDevicesViewModel
import com.kidmobi.mvvm.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class DeviceManagementFragment : Fragment(), Slider.OnSliderTouchListener {
    @Inject
    lateinit var device: MobileDevice
    private lateinit var binding: FragmentDeviceManagementBinding
    private val settingsViewModel: SettingsViewModel by viewModels()
    private val managedDevicesViewModel: ManagedDevicesViewModel by viewModels()
    private val args: DeviceManagementFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_device_management, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MobileAds.initialize(requireContext())
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        // Banner ID
        // ca-app-pub-9250940245734350/9369572410

        device = args.device

        binding.topAppBar.inflateMenu(R.menu.settings_top_app_bar)

        binding.topAppBar.setOnMenuItemClickListener {
            optionsItemSelected(it)
        }

        //settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)
        loadData()

        binding.screenBrightnessSlider.addOnSliderTouchListener(this)
        binding.soundVolumeSlider.addOnSliderTouchListener(this)

        binding.mobileDevice = device

    }

    private fun optionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.miDeleteMobileDevice -> deleteMobileDevice()
        }
        return true
    }

    private fun loadData() {
        settingsViewModel.getCurrentMobileDevice(device.deviceId)
        settingsViewModel.currentDevice
            .observe(viewLifecycleOwner, { currentDevice ->
                device = currentDevice
                Timber.d("$device")

                device.let {
                    binding.topAppBar.title = it.deviceOwnerName
                    binding.screenBrightnessSlider.value = it.settings.brightnessLevel
                    binding.soundVolumeSlider.value = it.settings.soundLevel
                }
            })
    }

    private suspend fun saveDeviceScreenBrightness(value: Float) {
        device.settings.brightnessLevel = value
        device = withContext(Dispatchers.Default) {
            settingsViewModel.saveDeviceScreenBrightness(device)
        }
        // settingsUtil.changeScreenBrightness(value)
    }

    fun turnBack(view: View) {
        activity?.finish()
    }

    override fun onStartTrackingTouch(slider: Slider) {

    }

    override fun onStopTrackingTouch(slider: Slider) {
        when (slider.id) {
            R.id.screenBrightnessSlider -> GlobalScope.launch { saveDeviceScreenBrightness(slider.value) }
            R.id.soundVolumeSlider -> GlobalScope.launch { saveDeviceSoundVolume(slider.value) }
        }
    }

    private suspend fun saveDeviceSoundVolume(value: Float) {
        device.settings.soundLevel = value
        device = withContext(Dispatchers.Default) {
            settingsViewModel.saveDeviceSoundVolume(device)
        }
    }

    private fun deleteMobileDevice() {

        Timber.d("This device will be deleted: $device")

        activity?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(device.deviceOwnerName)
                .setMessage(getString(R.string.are_you_sure_to_delete))
                .setNegativeButton(getString(R.string.no)) { dialog, which ->
                    dialog.cancel()
                }
                .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                    managedDevicesViewModel.deleteFromMyDevices(device.deviceId)
                    dialog.dismiss()
                    it.finish()
                }
                .show()
        }


    }
}