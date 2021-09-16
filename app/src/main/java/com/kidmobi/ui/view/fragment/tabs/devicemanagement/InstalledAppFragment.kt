package com.kidmobi.ui.view.fragment.tabs.devicemanagement

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.kidmobi.R
import com.kidmobi.data.model.InstalledApp
import com.kidmobi.databinding.FragmentInstalledAppBinding
import com.kidmobi.ui.view.adapter.InstalledAppRecyclerAdapter


class InstalledAppFragment : Fragment(), InstalledAppRecyclerAdapter.OnListItemClickListener {
    private lateinit var binding: FragmentInstalledAppBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_installed_app, container, false)
        return binding.root
    }

    private fun list2(): MutableList<ApplicationInfo> {
        val result = mutableListOf<ApplicationInfo>()
        val pm: PackageManager = requireContext().packageManager
        val installedApps = pm.getInstalledApplications(0)
        for (applicationInfo in installedApps) {
            if (applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
                // Users apps
                val icon = applicationInfo.loadIcon(pm)
                result.add(applicationInfo)
                println("-------------------------")
                println(applicationInfo.loadLabel(pm))
                println(applicationInfo.name)
                println(applicationInfo.packageName)
                println("-------------------------")
            }
        }

        return result
    }

    override fun onItemClick(app: InstalledApp) {
        println(app)
        println("Item is clicked now!!!")
    }
}