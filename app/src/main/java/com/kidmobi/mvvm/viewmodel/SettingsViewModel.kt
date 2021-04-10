package com.kidmobi.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kidmobi.assets.repositories.MobileDeviceRepo
import com.kidmobi.mvvm.model.MobileDevice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {
    var currentDevice = MutableLiveData<MobileDevice>()

    @Inject
    lateinit var mobileDeviceRepo: MobileDeviceRepo

    fun getCurrentMobileDevice(documentId: String) {
        CoroutineScope(Dispatchers.Default).launch {
            val temp = mobileDeviceRepo.getById(documentId)
            currentDevice.postValue(temp)
        }
    }

    fun saveDeviceScreenBrightness(device: MobileDevice): MobileDevice {
        CoroutineScope(Dispatchers.Default).launch {
            mobileDeviceRepo.update(
                device.deviceId,
                device
            )
        }
        return device
    }

    fun saveDeviceSoundVolume(device: MobileDevice): MobileDevice {
        CoroutineScope(Dispatchers.Default).launch {
            mobileDeviceRepo.update(
                device.deviceId,
                device
            )
        }
        return device
    }
}