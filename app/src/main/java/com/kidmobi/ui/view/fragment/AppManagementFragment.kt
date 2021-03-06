package com.kidmobi.ui.view.fragment

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kidmobi.R
import com.kidmobi.business.utils.misc.SharedPrefsUtil
import com.kidmobi.data.model.DeviceSession
import com.kidmobi.data.model.InstalledApp
import com.kidmobi.data.model.MobileDevice
import com.kidmobi.databinding.FragmentAppManagementBinding
import com.kidmobi.ui.viewmodel.DeviceSessionViewModel
import com.kidmobi.ui.viewmodel.MobileDeviceViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class AppManagementFragment : BottomSheetDialogFragment() {
    lateinit var device: MobileDevice
    private lateinit var app: InstalledApp
    private lateinit var binding: FragmentAppManagementBinding
    private val viewModel: DeviceSessionViewModel by viewModels()
    private val mobileDeviceViewModel: MobileDeviceViewModel by viewModels()
    private val args: AppManagementFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): BottomSheetDialog {
        return object : BottomSheetDialog(requireActivity()) {
            override fun onBackPressed() {
                cancelDialog
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_app_management, container, false)
        requireDialog().setCancelable(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        device = args.device
        app = args.app
        Timber.d("Argument is came: $device")

        binding.btnSaveSession.setOnClickListener(setTimeLimitation)
        binding.btnDialogCancel.setOnClickListener(cancelDialog)

    }


    override fun onResume() {
        super.onResume()
        requireDialog().setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                cancelDialog
                return@setOnKeyListener true
            }

            return@setOnKeyListener false
        }
    }


    private val cancelDialog: (v: View) -> Unit = {
        dismiss()
        //findNavController().navigate(R.id.action_appManagementFragment_to_deviceManagementFragment)
    }

    private val setTimeLimitation: (v: View) -> Unit = {
        val session = DeviceSession()
        val minutes = when (binding.cgDeviceSession.checkedChipId) {
            binding.chip1.id -> 30
            binding.chip2.id -> 60
            binding.chip3.id -> 120
            binding.chip4.id -> 240
            else -> 0
        }

        device.session = session

        //mobileDeviceViewModel.updateDevice(device)
        //viewModel.sessionStart(session)
        Timber.d("Limitation is set!")
        dismiss()
    }

}