package com.kidmobi.ui.view.fragment.tabs.devicemanagement

import android.content.ContentValues
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
import com.kidmobi.databinding.FragmentDeviceManagementWebTabBinding
import com.kidmobi.ui.view.adapter.InstalledAppRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


@AndroidEntryPoint
class InstalledAppFragment : Fragment(), InstalledAppRecyclerAdapter.OnListItemClickListener {
    private lateinit var binding: FragmentDeviceManagementWebTabBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_device_management_web_tab, container, false)
        return binding.root
    }

    private fun getBrowserHistory() {
        val resolver = requireActivity().contentResolver
        val projection = arrayOf(
            UserDictionary.Words._ID,
            UserDictionary.Words.LOCALE,
            UserDictionary.Words.WORD,
            UserDictionary.Words.FREQUENCY,
            UserDictionary.Words.APP_ID
        )

        val sortOrder = UserDictionary.Words.LOCALE
        val cursor = resolver.query(UserDictionary.Words.CONTENT_URI, projection, null, null, sortOrder)


        cursor.let { cr ->
            val data = StringBuffer()

            println("****************************************")
            if (cr != null) {
                while (cr.moveToNext()) {
                    data.append(cr.getString(1) + "," + cr.getString(2) + ", " + cr.getString(3))
                    data.append("\n")
                    println(data)
                }
            }
            println("****************************************")
        }
        cursor?.close()
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