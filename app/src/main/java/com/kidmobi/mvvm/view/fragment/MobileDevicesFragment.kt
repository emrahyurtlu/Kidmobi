package com.mobicon.android.mvvm.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kidmobi.R
import com.kidmobi.assets.adapter.MobileDeviceRecyclerAdapter
import com.kidmobi.mvvm.model.MobileDevice
import com.kidmobi.mvvm.view.SettingsActivity
import com.kidmobi.mvvm.viewmodel.UserMobileDeviceViewModel
import com.kidmobi.assets.utils.printsln

class MobileDevicesFragment : Fragment(),
    MobileDeviceRecyclerAdapter.OnMyDeviceItemClickListener {
    private val TAG = "MobileDevicesFragment"
    private var devices: MutableList<MobileDevice> = mutableListOf()
    private lateinit var adapter: MobileDeviceRecyclerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var textView: TextView
    private lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var viewModel: UserMobileDeviceViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentView = inflater.inflate(R.layout.fragment_mydevices, container, false)

        recyclerView = fragmentView.findViewById(R.id.my_devices_rc)
        textView = fragmentView.findViewById(R.id.no_mobile_device)
        refreshLayout = fragmentView.findViewById(R.id.mobileDevicesSwipeRefresh)
        refreshLayout.setOnRefreshListener {
            loadData()
            refreshLayout.isRefreshing = false
        }

        adapter = MobileDeviceRecyclerAdapter(devices, this)

        viewModel = ViewModelProviders.of(this).get(UserMobileDeviceViewModel::class.java)
        loadData()

        recyclerView.let {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter
        }

        return fragmentView
    }

    private fun loadData() {
        viewModel.getUserMobileDevices()
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
        if (devices.size == 0) {
            recyclerView.visibility = View.GONE
            textView.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            textView.visibility = View.GONE
        }
    }

    override fun onItemClick(device: MobileDevice) {
        val intent = Intent(context, SettingsActivity::class.java)
        intent.putExtra("device", device)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        loadData()
        Log.d(TAG, "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        loadData()
        Log.d(TAG, "onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
        loadData()
    }
}