package com.kidmobi.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.kidmobi.assets.repositories.MobileDeviceRepo
import com.kidmobi.assets.utils.printsln
import com.kidmobi.mvvm.model.MobileDevice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MobileDeviceActivityViewModel @Inject constructor(var mobileDeviceRepo: MobileDeviceRepo) : ViewModel() {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun saveDeviceDetails(device: MobileDevice) {
        printsln("MobileDeviceActivityViewModel::saveDeviceDetails => $device")
        uiScope.launch {
            val calendar = Calendar.getInstance()
            device.updatedAt = calendar.time
            mobileDeviceRepo.update(device.deviceId, device)
        }
    }
}