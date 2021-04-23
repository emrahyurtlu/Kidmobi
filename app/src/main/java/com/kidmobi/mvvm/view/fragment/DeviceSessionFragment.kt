package com.kidmobi.mvvm.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kidmobi.databinding.FragmentDeviceSessionBinding

class DeviceSessionFragment : Fragment() {

    private lateinit var binding: FragmentDeviceSessionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeviceSessionBinding.inflate(inflater)
        return binding.root
    }
}