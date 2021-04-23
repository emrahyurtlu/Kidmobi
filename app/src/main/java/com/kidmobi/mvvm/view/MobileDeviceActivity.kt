package com.kidmobi.mvvm.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.kidmobi.R
import com.kidmobi.assets.utils.extensions.modelExtensions.init
import com.kidmobi.assets.utils.extensions.modelExtensions.isNull
import com.kidmobi.databinding.ActivityMobileDeviceBinding
import com.kidmobi.mvvm.model.MobileDevice
import com.kidmobi.mvvm.viewmodel.ManagedDevicesViewModel
import com.kidmobi.mvvm.viewmodel.MobileDeviceActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MobileDeviceActivity : AppCompatActivity() {

    var mobileDevice: MobileDevice = MobileDevice().init()

    private val viewModel: MobileDeviceActivityViewModel by viewModels()
    private val managedDevicesViewModel: ManagedDevicesViewModel by viewModels()

    private lateinit var binding: ActivityMobileDeviceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMobileDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mobileDevice = intent.getSerializableExtra("device") as MobileDevice

        if (mobileDevice.isNull())
            finish()

        binding.deviceOwner.setText(mobileDevice.deviceOwnerName)

        // Admob ID: ca-app-pub-9250940245734350/2801074884
    }

    fun turnBack(view: View) {
        finish()
    }

    fun saveDeviceDetails(view: View) {

        if (binding.deviceOwner.text.isNullOrEmpty()) {
            binding.deviceOwner.setError(
                getString(R.string.form_field_not_null),
                ResourcesCompat.getDrawable(
                    baseContext.resources,
                    R.drawable.ic_baseline_error_24,
                    null
                )
            )
            return
        }

        mobileDevice.deviceOwnerName = binding.deviceOwner.text.toString()

        viewModel.saveDeviceDetails(mobileDevice)
        managedDevicesViewModel.addManagedDeviceList(mobileDevice.deviceId)

        turnBack(view)

    }
}