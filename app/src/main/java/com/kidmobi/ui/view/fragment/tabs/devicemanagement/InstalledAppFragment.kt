package com.kidmobi.ui.view.fragment.tabs.devicemanagement

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.UserDictionary
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.kidmobi.R
import com.kidmobi.data.model.InstalledApp
import com.kidmobi.databinding.FragmentDeviceManagementInstalledAppTabBinding
import com.kidmobi.ui.view.adapter.InstalledAppRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class InstalledAppFragment : Fragment(), InstalledAppRecyclerAdapter.OnListItemClickListener {
    private lateinit var binding: FragmentDeviceManagementInstalledAppTabBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_device_management_installed_app_tab, container, false)
        return binding.root
    }

    override fun onItemClick(app: InstalledApp) {
        println(app)
        println("Item is clicked now!!!")
    }
}