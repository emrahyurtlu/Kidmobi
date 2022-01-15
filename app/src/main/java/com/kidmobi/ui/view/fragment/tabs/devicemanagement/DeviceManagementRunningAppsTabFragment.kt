package com.kidmobi.ui.view.fragment.tabs.devicemanagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.kidmobi.R
import com.kidmobi.business.utils.misc.RunningAppsUtil
import com.kidmobi.databinding.FragmentDeviceManagementRunningAppTabBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DeviceManagementRunningAppsTabFragment : Fragment() {
    private lateinit var binding: FragmentDeviceManagementRunningAppTabBinding
    private lateinit var appUtil: RunningAppsUtil

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        appUtil = RunningAppsUtil(requireContext())
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_device_management_running_app_tab, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAppList.setOnClickListener { listInstalledApps() }
    }

    private fun listInstalledApps() {
        val list = appUtil.getList()
        println(list)
        if (list.isNotEmpty()) {
            binding.tvListCount.text = list.get(0).packageName
        }

    }
}