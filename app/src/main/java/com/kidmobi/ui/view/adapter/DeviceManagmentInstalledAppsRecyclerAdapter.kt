package com.kidmobi.ui.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kidmobi.R
import com.kidmobi.data.model.InstalledApp
import com.kidmobi.ui.view.fragment.tabs.devicemanagement.DeviceManagementInstalledAppsTabFragment

class DeviceManagmentInstalledAppsRecyclerAdapter(
    private var installedApps: MutableList<InstalledApp>,
    private val listener: DeviceManagementInstalledAppsTabFragment
) :
    RecyclerView.Adapter<DeviceManagmentInstalledAppsRecyclerAdapter.InstalledAppViewHolder>() {
    interface OnMyDeviceItemClickListener {
        fun onItemClick(installedApp: InstalledApp)
    }

    inner class InstalledAppViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val packageName: TextView = view.findViewById(R.id.packageName)
        private val appName: TextView = view.findViewById(R.id.appName)
        private val container: CardView = view.findViewById(R.id.installed_app_cv)

        fun bind(installedApp: InstalledApp, listener: OnMyDeviceItemClickListener) {
            container.setOnClickListener {
                listener.onItemClick(installedApp)
            }
            appName.text = installedApp.appName
            packageName.text = installedApp.packageName
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InstalledAppViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_installed_app_list_layout, parent, false)
        return InstalledAppViewHolder(view)
    }

    override fun onBindViewHolder(holder: InstalledAppViewHolder, position: Int) {
        val installedApp: InstalledApp = installedApps[position]
        holder.bind(installedApp, listener)
    }

    override fun getItemCount() = installedApps.size
}

