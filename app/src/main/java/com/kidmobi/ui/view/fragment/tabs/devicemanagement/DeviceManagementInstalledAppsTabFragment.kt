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
import com.kidmobi.R
import com.kidmobi.business.utils.misc.InstalledAppsUtil
import com.kidmobi.data.model.MobileDevice
import com.kidmobi.databinding.FragmentDeviceManagementInstalledAppTabBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class DeviceManagementInstalledAppsTabFragment(var device: MobileDevice) : Fragment() {
    private lateinit var binding: FragmentDeviceManagementInstalledAppTabBinding
    private lateinit var adapter: ArrayAdapter<String>
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

        val items = mutableListOf<String>()
        device.apps.forEach { item ->
            items.add(item.appName)
        }

        if (items.isEmpty())
            items.add(getString(R.string.installed_apps_not_found))

        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, items)
        binding.lstInstalledApps.adapter = adapter

        binding.lstInstalledApps.setOnItemClickListener { parent, view, position, id ->
            println(items.get(position) + " is clicked!!!!!!!")
        }
    }
}