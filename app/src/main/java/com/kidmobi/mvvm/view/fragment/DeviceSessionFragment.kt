package com.kidmobi.mvvm.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.kidmobi.R
import com.kidmobi.databinding.FragmentDeviceSessionBinding
import com.kidmobi.mvvm.model.DeviceSession
import com.kidmobi.mvvm.model.MobileDevice
import com.kidmobi.mvvm.viewmodel.DeviceSessionViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class DeviceSessionFragment : Fragment() {
    lateinit var device: MobileDevice
    private lateinit var binding: FragmentDeviceSessionBinding
    private val viewModel: DeviceSessionViewModel by viewModels()
    private val args: DeviceSessionFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_device_session, container, false)
        binding.topAppBar.setupWithNavController(findNavController())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        device = args.device
        binding.btnSaveSession.setOnClickListener {
            createSession(it)
        }
    }

    private fun createSession(view: View) {
        Timber.d("Session is created!")
        val calendar = Calendar.getInstance()
        val session = DeviceSession()
        val minutes = when (binding.cgDeviceSession.checkedChipId) {
            binding.chip1.id -> 30
            binding.chip2.id -> 60
            binding.chip3.id -> 120
            binding.chip4.id -> 240
            else -> 10
        }
        session.apply {
            done = false
            sessionStart = calendar.time
            calendar.add(Calendar.MINUTE, minutes)
            sessionEnd = calendar.time
            sessionCreatorDeviceId = FirebaseAuth.getInstance().currentUser?.uid.toString()
            sessionOwnerDeviceId = device.deviceId
        }

        viewModel.sessionStart(session)

        Snackbar.make(view, "Oturum açıldı", Snackbar.LENGTH_LONG).show().also {
            findNavController().navigate(DeviceSessionFragmentDirections.actionDeviceSessionFragmentToDeviceManagementFragment(device))
        }

    }

}