package com.kidmobi.mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kidmobi.assets.repositories.MobileDeviceRepo
import com.kidmobi.mvvm.model.MobileDevice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    private var _currentDevice = MutableLiveData<MobileDevice>()
    val currentDevice: LiveData<MobileDevice>
        get() = _currentDevice

    @Inject
    lateinit var mobileDeviceRepo: MobileDeviceRepo

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun getCurrentMobileDevice(documentId: String) {
        uiScope.launch {
            val temp = mobileDeviceRepo.getById(documentId)
            _currentDevice.postValue(temp)
        }
    }

    fun saveDeviceScreenBrightness(device: MobileDevice): MobileDevice {
        uiScope.launch {
            mobileDeviceRepo.update(
                device.deviceId,
                device
            )
        }
        return device
    }

    fun saveDeviceSoundVolume(device: MobileDevice): MobileDevice {
        uiScope.launch {
            mobileDeviceRepo.update(
                device.deviceId,
                device
            )
        }
        return device
    }
}