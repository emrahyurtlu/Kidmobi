package com.kidmobi.ui.view.fragment.tabs.devicemanagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.kidmobi.R
import com.kidmobi.data.model.MobileDevice
import com.kidmobi.databinding.FragmentDeviceManagementRunningAppTabBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DeviceManagementRunningAppsTabFragment(var device: MobileDevice) : Fragment() {
    private lateinit var binding: FragmentDeviceManagementRunningAppTabBinding
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_device_management_running_app_tab, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val items = mutableListOf<String>()
        println("*********************")
        println(device.runningApps)
        println("*********************")
        device.runningApps.forEach { item ->
            items.add(item.appName)
        }

        if (items.isEmpty())
            items.add(getString(R.string.running_app_not_found))

        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, items)
        binding.lstRunningApps.adapter = adapter
    }
}