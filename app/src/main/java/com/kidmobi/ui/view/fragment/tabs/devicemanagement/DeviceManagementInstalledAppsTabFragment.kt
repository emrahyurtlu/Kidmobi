package com.kidmobi.ui.view.fragment.tabs.devicemanagement

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kidmobi.R
import com.kidmobi.business.utils.misc.InstalledAppsUtil
import com.kidmobi.data.model.InstalledApp
import com.kidmobi.data.model.MobileDevice
import com.kidmobi.databinding.FragmentDeviceManagementInstalledAppTabBinding
import com.kidmobi.ui.view.adapter.DeviceManagmentInstalledAppsRecyclerAdapter
import com.kidmobi.ui.view.adapter.MobileDeviceRecyclerAdapter
import com.kidmobi.ui.view.fragment.DashboardFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class DeviceManagementInstalledAppsTabFragment(var device: MobileDevice) : Fragment(), DeviceManagmentInstalledAppsRecyclerAdapter.OnMyDeviceItemClickListener {
    private lateinit var binding: FragmentDeviceManagementInstalledAppTabBinding
    private lateinit var adapter: DeviceManagmentInstalledAppsRecyclerAdapter
    private var items = mutableListOf<String>()

    @Inject
    lateinit var appUtil: InstalledAppsUtil

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_device_management_installed_app_tab, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DeviceManagmentInstalledAppsRecyclerAdapter(device.apps, this)

        binding.rvInstalledApp.let {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = adapter
        }
    }

    override fun onItemClick(installedApp: InstalledApp) {
        println(installedApp)
        /*findNavController().navigate(
            DashboardFragmentDirections.actionDashboardFragmentToDeviceManagementFragment(device)
        )*/
    }
}