package com.kidmobi.mvvm.view

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.slider.Slider
import com.kidmobi.R
import com.kidmobi.assets.utils.SettingsUtil
import com.kidmobi.assets.utils.printsln
import com.kidmobi.databinding.ActivitySettingsBinding
import com.kidmobi.mvvm.model.MobileDevice
import com.kidmobi.mvvm.viewmodel.ManagedDevicesViewModel
import com.kidmobi.mvvm.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity(), Slider.OnSliderTouchListener {

    @Inject
    lateinit var device: MobileDevice

    @Inject
    lateinit var settingsUtil: SettingsUtil

    private lateinit var binding: ActivitySettingsBinding

    private val settingsViewModel: SettingsViewModel by viewModels()

    private val managedDevicesViewModel: ManagedDevicesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MobileAds.initialize(this)
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        // Banner ID
        // ca-app-pub-9250940245734350/9369572410

        device = intent.getSerializableExtra("device") as MobileDevice

        //settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)
        loadData()

        binding.screenBrightnessSlider.addOnSliderTouchListener(this)
        binding.soundVolumeSlider.addOnSliderTouchListener(this)
    }

    private fun loadData() {
        settingsViewModel.getCurrentMobileDevice(device.deviceId)
        settingsViewModel.currentDevice
            .observe(this, { currentDevice ->
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
        finish()
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
        // settingsUtil.changeDeviceSound(value)
    }

    fun deleteMobileDevice(item: MenuItem) {

        printsln("This device will be deleted: $device")

        MaterialAlertDialogBuilder(this)
            .setTitle(device.deviceOwnerName)
            .setMessage(getString(R.string.are_you_sure_to_delete))
            .setNegativeButton(getString(R.string.no)) { dialog, which ->
                dialog.cancel()
            }
            .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                CoroutineScope(Dispatchers.Default).launch {
                    managedDevicesViewModel.deleteFromMyDevices(device.deviceId)
                }
                dialog.dismiss()
                finish()
            }
            .show()

    }
}