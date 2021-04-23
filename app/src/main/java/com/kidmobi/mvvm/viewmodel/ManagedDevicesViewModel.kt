package com.kidmobi.mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kidmobi.assets.repositories.ManagedDeviceRepo
import com.kidmobi.assets.utils.printsln
import com.kidmobi.mvvm.model.ManagedDevice
import com.kidmobi.mvvm.model.MobileDevice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManagedDevicesViewModel @Inject constructor() : ViewModel() {

    private var _userMobileDevice = MutableLiveData<ManagedDevice>()
    val managedDevice: LiveData<ManagedDevice>
        get() = _userMobileDevice

    private var _mobileDeviceList = MutableLiveData<MutableList<MobileDevice>>()
    val mobileDeviceList: LiveData<MutableList<MobileDevice>>
        get() = _mobileDeviceList

    @Inject
    lateinit var managedDeviceRepo: ManagedDeviceRepo

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun getManagedDevice() {
        uiScope.launch {
            val temp = managedDeviceRepo.getByCurrentUserId()
            _userMobileDevice.postValue(temp)
        }
    }

    fun getManagedMobileDevices() {
        uiScope.launch {
            val temp = managedDeviceRepo.getListOfUserDevices()
            _mobileDeviceList.postValue(temp)
        }
    }

    fun addManagedDeviceList(deviceId: String) {
        uiScope.launch {
            val managedDevice = managedDeviceRepo.getByCurrentUserId()
            printsln("ManagedDevicesViewModel::addManagedDeviceList => $managedDevice")
            val set = managedDevice.devices.toMutableSet()
            set.add(deviceId)
            managedDevice.devices = set.toMutableList()
            printsln("ManagedDevicesViewModel::addManagedDeviceList => $managedDevice")
            managedDeviceRepo.updateCurrentUserDeviceList(managedDevice)
            printsln("Device is added to collection!")

            _mobileDeviceList.postValue(managedDeviceRepo.getListOfUserDevices())
        }
    }

    fun deleteFromMyDevices(documentId: String) {
        uiScope.launch {
            managedDeviceRepo.remove(documentId)
            getManagedDevice()
            getManagedMobileDevices()
        }
    }


}