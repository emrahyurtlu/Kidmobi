package com.kidmobi.ui.view.fragment.tabs.devicemanagement

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.kidmobi.R
import com.kidmobi.business.observers.WebHistoryObserver
import com.kidmobi.databinding.FragmentDeviceManagementWebTabBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DeviceManagementWebTabFragment : Fragment() {
    private lateinit var binding: FragmentDeviceManagementWebTabBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_device_management_web_tab, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBrowserHistory()
    }

    private fun getBrowserHistory() {
        val observer = WebHistoryObserver(requireContext())
        requireContext().contentResolver.registerContentObserver(Uri.parse("content://com.android.chrome.browser/history"), true, observer)
    }
}