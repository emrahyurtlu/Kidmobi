package com.kidmobi.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kidmobi.assets.repositories.MobileDeviceRepo
import com.kidmobi.mvvm.model.MobileDevice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {
    var currentDevice = MutableLiveData<MobileDevice>()
    private val mobileDeviceRepo: MobileDeviceRepo = MobileDeviceRepo()

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