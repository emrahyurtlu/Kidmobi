package com.kidmobi.mvvm.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kidmobi.assets.adapter.MobileDeviceRecyclerAdapter
import com.kidmobi.assets.utils.printsln
import com.kidmobi.databinding.FragmentMobileDevicesBinding
import com.kidmobi.mvvm.model.MobileDevice
import com.kidmobi.mvvm.view.SettingsActivity
import com.kidmobi.mvvm.viewmodel.UserMobileDeviceViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MobileDevicesFragment : Fragment(),
    MobileDeviceRecyclerAdapter.OnMyDeviceItemClickListener {
    private var devices: MutableList<MobileDevice> = mutableListOf()
    private lateinit var binding: FragmentMobileDevicesBinding
    private lateinit var adapter: MobileDeviceRecyclerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var textView: TextView
    private lateinit var refreshLayout: SwipeRefreshLayout

    private val viewModel: UserMobileDeviceViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMobileDevicesBinding.inflate(inflater)

        recyclerView = binding.myDevicesRc
        textView = binding.noMobileDevice
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