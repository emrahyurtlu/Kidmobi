package com.kidmobi.ui.view.fragment.tabs.devicemanagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.slider.Slider
import com.kidmobi.R
import com.kidmobi.business.utils.extensions.modelExtensions.isInvalid
import com.kidmobi.business.utils.extensions.modelExtensions.isNull
import com.kidmobi.data.model.MobileDevice
import com.kidmobi.databinding.FragmentDeviceManagementGeneralTabBinding
import com.kidmobi.ui.view.fragment.DeviceManagementFragmentDirections
import com.kidmobi.ui.viewmodel.DeviceSessionViewModel
import com.kidmobi.ui.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@AndroidEntryPoint
class DeviceManagementGeneralTabFragment(var device: MobileDevice) : Fragment(), Slider.OnSliderTouchListener {
    private lateinit var binding: FragmentDeviceManagementGeneralTabBinding
    private val settingsViewModel: SettingsViewModel by viewModels()
    private val sessionViewModel: DeviceSessionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_device_management_general_tab, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.screenBrightnessSlider.addOnSliderTouchListener(this)
        binding.soundVolumeSlider.addOnSliderTouchListener(this)
        binding.mobileDevice = device
        loadData()
        checkSession()
    }

    private fun checkSession() {
        sessionViewModel.getSession(device.deviceId)
        sessionViewModel.currentSession.observe(viewLifecycleOwner, { session ->
            Timber.d("Session: $session")
            Timber.d("Session isNull: ${session.isNull()}")
            Timber.d("Session isInvalid: ${session.isInvalid()}")

            if (session.isNull() || session.isInvalid()) {
                findNavController().navigate(DeviceManagementFragmentDirections.actionDeviceManagementFragmentToDeviceSessionFragment(device))
            }
        })
    }

    private fun loadData() {
        settingsViewModel.getCurrentMobileDevice(device.deviceId)
        settingsViewModel.currentDevice
            .observe(viewLifecycleOwner, { currentDevice ->
                binding.mobileDevice = currentDevice
                device = currentDevice
                Timber.d("$device")

                device.let {
                    //binding.topAppBar.title = it.deviceOwnerName
                    binding.screenBrightnessSlider.value = it.settings.brightnessLevel
                    binding.soundVolumeSlider.value = it.settings.soundLevel
                }
            })
    }

    override fun onStartTrackingTouch(slider: Slider) {

    }

    override fun onStopTrackingTouch(slider: Slider) {
        when (slider.id) {
            R.id.screenBrightnessSlider -> GlobalScope.launch { saveDeviceScreenBrightness(slider.value) }
            R.id.soundVolumeSlider -> GlobalScope.launch { saveDeviceSoundVolume(slider.value) }
        }
    }

    private suspend fun saveDeviceScreenBrightness(value: Float) {
        device.settings.brightnessLevel = value
        device = withContext(Dispatchers.Default) {
            settingsViewModel.saveDeviceScreenBrightness(device)
        }
    }

    private suspend fun saveDeviceSoundVolume(value: Float) {
        device.settings.soundLevel = value
        device = withContext(Dispatchers.Default) {
            settingsViewModel.saveDeviceSoundVolume(device)
        }
    }
}