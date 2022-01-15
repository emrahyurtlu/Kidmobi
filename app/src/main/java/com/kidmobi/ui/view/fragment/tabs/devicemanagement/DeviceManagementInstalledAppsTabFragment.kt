package com.kidmobi.ui.view.fragment.tabs.devicemanagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.kidmobi.R
import com.kidmobi.business.utils.misc.InstalledAppsUtil
import com.kidmobi.databinding.FragmentDeviceManagementInstalledAppTabBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class DeviceManagementInstalledAppsTabFragment : Fragment() {
    private lateinit var binding: FragmentDeviceManagementInstalledAppTabBinding

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
        binding.btnAppList.setOnClickListener { listInstalledApps() }
    }

    private fun listInstalledApps() {
        val list = appUtil.getList(requireContext())
        println("****************************************")
        println("****************************************")
        println("****************************************")
        Timber.d(list.count().toString())
        Timber.d(list.toString())
        println(list)
        println("****************************************")
        println("****************************************")
        println("****************************************")
    }
}