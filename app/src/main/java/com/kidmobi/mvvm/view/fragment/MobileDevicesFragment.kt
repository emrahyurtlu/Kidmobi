package com.kidmobi.mvvm.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kidmobi.R
import com.kidmobi.assets.adapter.MobileDeviceRecyclerAdapter
import com.kidmobi.assets.utils.printsln
import com.kidmobi.databinding.FragmentMobileDevicesBinding
import com.kidmobi.mvvm.model.MobileDevice
import com.kidmobi.mvvm.viewmodel.ManagedDevicesViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MobileDevicesFragment : Fragment(),
    MobileDeviceRecyclerAdapter.OnMyDeviceItemClickListener {
    private var devices: MutableList<MobileDevice> = mutableListOf()
    private lateinit var binding: FragmentMobileDevicesBinding
    private lateinit var adapter: MobileDeviceRecyclerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var refreshLayout: SwipeRefreshLayout

    private val viewModel: ManagedDevicesViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mobile_devices, container, false)

        recyclerView = binding.myDevicesRc
        refreshLayout = binding.mobileDevicesSwipeRefresh
        refreshLayout.setOnRefreshListener {
            loadData()
            refreshLayout.isRefreshing = false
        }

        adapter = MobileDeviceRecyclerAdapter(devices, this)

        //viewModel = ViewModelProviders.of(this).get(UserMobileDeviceViewModel::class.java)
        loadData()

        recyclerView.let {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter
        }

        return binding.root
    }

    private fun loadData() {
        viewModel.getManagedMobileDevices()
        viewModel.mobileDeviceList
            .observe(viewLifecycleOwner, { list ->
                devices.clear()
                devices.addAll(list)
                adapter.notifyDataSetChanged()
                setEmptyDeviceListMessage()
                printsln(devices, "MobileDevicesFragment::loadData()")
            })
    }

    private fun setEmptyDeviceListMessage() {
        Timber.d("Checking whether list is empty: ${devices.isEmpty()}")
        if (devices.isEmpty()) {
            binding.myDevicesRc.visibility = View.GONE
            binding.noMobileDevice.visibility = View.VISIBLE
        } else {
            binding.myDevicesRc.visibility = View.VISIBLE
            binding.noMobileDevice.visibility = View.GONE
        }
    }

    override fun onItemClick(device: MobileDevice) {
        findNavController().navigate(
            DashboardFragmentDirections.actionDashboardFragmentToDeviceManagementFragment(device)
        )
    }

    override fun onStart() {
        super.onStart()
        loadData()
        Timber.d("onStart: ")
    }

    override fun onResume() {
        super.onResume()
        loadData()
        Timber.d("onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Timber.d("onPause: ")
        loadData()
    }
}