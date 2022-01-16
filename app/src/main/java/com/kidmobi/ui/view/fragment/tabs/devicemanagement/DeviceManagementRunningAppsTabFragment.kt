package com.kidmobi.ui.view.fragment.tabs.devicemanagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kidmobi.R
import com.kidmobi.data.model.InstalledApp
import com.kidmobi.data.model.MobileDevice
import com.kidmobi.ui.view.adapter.DeviceManagmentRunningAppsRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DeviceManagementRunningAppsTabFragment(var device: MobileDevice) : Fragment(), DeviceManagmentRunningAppsRecyclerAdapter.OnMyDeviceItemClickListener {
    private lateinit var binding: com.kidmobi.databinding.FragmentDeviceManagementRunningAppTabBinding
    private lateinit var adapter: DeviceManagmentRunningAppsRecyclerAdapter

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

        adapter = DeviceManagmentRunningAppsRecyclerAdapter(device.runningApps, this)
        binding.rvInstalledApp.let {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = adapter
        }
    }

    override fun onItemClick(installedApp: InstalledApp) {
        println(installedApp)
    }
}