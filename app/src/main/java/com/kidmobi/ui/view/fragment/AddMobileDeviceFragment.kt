package com.kidmobi.ui.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.kidmobi.R
import com.kidmobi.business.utils.extensions.modelExtensions.isNull
import com.kidmobi.business.utils.extensions.redirectIfNull
import com.kidmobi.data.model.MobileDevice
import com.kidmobi.databinding.FragmentAddMobileDeviceBinding
import com.kidmobi.ui.viewmodel.ManagedDevicesViewModel
import com.kidmobi.ui.viewmodel.MobileDeviceActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddMobileDeviceFragment : Fragment() {
    private lateinit var binding: FragmentAddMobileDeviceBinding
    var mobileDevice: MobileDevice = MobileDevice()
    private val viewModel: MobileDeviceActivityViewModel by viewModels()
    private val managedDevicesViewModel: ManagedDevicesViewModel by viewModels()
    private val args: AddMobileDeviceFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_mobile_device, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.redirectIfNull(args.device, R.id.action_addMobileDeviceFragment_to_dashboardFragment)

        mobileDevice = args.device

        if (mobileDevice.isNull())
            findNavController().navigate(R.id.action_addMobileDeviceFragment_to_dashboardFragment)

        binding.deviceOwner.setText(mobileDevice.deviceOwnerName)

        binding.btnAddDevice.setOnClickListener(saveDeviceDetails)
    }

    /*private fun setUpAds() {
        MobileAds.initialize(requireContext())
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }*/

    private val saveDeviceDetails: (v: View) -> Unit = {
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
        } else {
            mobileDevice.deviceOwnerName = binding.deviceOwner.text.toString()
            viewModel.saveDeviceDetails(mobileDevice)
            managedDevicesViewModel.addManagedDeviceList(mobileDevice.deviceId)
            findNavController().navigate(R.id.action_addMobileDeviceFragment_to_dashboardFragment)
        }
    }
}