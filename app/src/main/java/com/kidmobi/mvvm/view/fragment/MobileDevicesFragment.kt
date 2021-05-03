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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kidmobi.R
import com.kidmobi.business.adapter.MobileDeviceRecyclerAdapter
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
    private lateinit var refreshLayout: SwipeRefreshLayout

    private val viewModel: ManagedDevicesViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mobile_devices, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshLayout = binding.mobileDevicesSwipeRefresh
        refreshLayout.setOnRefreshListener {
            loadData()
            refreshLayout.isRefreshing = false
        }

        adapter = MobileDeviceRecyclerAdapter(devices, this)

        loadData()

        binding.rvMobileDevices.let {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = adapter
        }
    }

    private fun loadData() {
        viewModel.getManagedMobileDevices()
        viewModel.mobileDeviceList
            .observe(viewLifecycleOwner, { list ->
                devices.clear()
                devices.addAll(list)
                adapter.notifyDataSetChanged()
                setEmptyDeviceListMessage(list.size)
            })
    }

    private fun setEmptyDeviceListMessage(size: Int) {
        Timber.d("Checking whether list size : $size")
        if (size == 0) {
            binding.rvMobileDevices.visibility = View.INVISIBLE
            binding.tvNoMobileDevice.visibility = View.VISIBLE
        }
        if (size > 0) {
            binding.rvMobileDevices.visibility = View.VISIBLE
            binding.tvNoMobileDevice.visibility = View.INVISIBLE
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