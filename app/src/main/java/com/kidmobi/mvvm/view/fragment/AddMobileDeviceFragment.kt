package com.kidmobi.mvvm.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kidmobi.R
import com.kidmobi.assets.utils.extensions.modelExtensions.init
import com.kidmobi.assets.utils.extensions.modelExtensions.isNull
import com.kidmobi.databinding.FragmentAddMobileDeviceBinding
import com.kidmobi.mvvm.model.MobileDevice
import com.kidmobi.mvvm.viewmodel.ManagedDevicesViewModel
import com.kidmobi.mvvm.viewmodel.MobileDeviceActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddMobileDeviceFragment : Fragment() {
    private lateinit var binding: FragmentAddMobileDeviceBinding

    var mobileDevice: MobileDevice = MobileDevice().init()

    private val viewModel: MobileDeviceActivityViewModel by viewModels()

    private val managedDevicesViewModel: ManagedDevicesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_mobile_device, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            mobileDevice = it.intent.getSerializableExtra("device") as MobileDevice

            if (mobileDevice.isNull())
                it.finish()
        }



        binding.deviceOwner.setText(mobileDevice.deviceOwnerName)

        // Admob ID: ca-app-pub-9250940245734350/2801074884
    }

    fun turnBack(view: View) {
        activity?.finish()
    }

    fun saveDeviceDetails(view: View) {

        if (binding.deviceOwner.text.isNullOrEmpty()) {
            binding.deviceOwner.setError(
                getString(R.string.form_field_not_null),
                context?.resources?.let {
                    ResourcesCompat.getDrawable(
                        it,
                        R.drawable.ic_baseline_error_24,
                        null
                    )
                }
            )
            return
        }

        mobileDevice.deviceOwnerName = binding.deviceOwner.text.toString()

        viewModel.saveDeviceDetails(mobileDevice)
        managedDevicesViewModel.addManagedDeviceList(mobileDevice.deviceId)

        turnBack(view)

    }
}