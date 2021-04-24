package com.kidmobi.mvvm.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kidmobi.R
import com.kidmobi.databinding.FragmentDeviceSessionBinding
import com.kidmobi.mvvm.model.DeviceSession
import com.kidmobi.mvvm.viewmodel.DeviceSessionViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class DeviceSessionFragment : Fragment() {
    private lateinit var binding: FragmentDeviceSessionBinding
    private val viewModel: DeviceSessionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_device_session, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSaveSession.setOnClickListener {
            createSession()
        }
    }

    private fun createSession() {
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
            sessionCreatorDeviceId = ""
            sessionOwnerDeviceId = ""
        }

        viewModel.sessionStart(session)
    }
}