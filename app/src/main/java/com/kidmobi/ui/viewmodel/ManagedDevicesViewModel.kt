package com.kidmobi.ui.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kidmobi.data.model.ManagedDevice
import com.kidmobi.data.model.MobileDevice
import com.kidmobi.data.repositories.ManagedDeviceRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ManagedDevicesViewModel @Inject constructor(private var managedDeviceRepo: ManagedDeviceRepo) : ViewModel() {

    private var _userMobileDevice = MutableLiveData<ManagedDevice>()
    val managedDevice: LiveData<ManagedDevice>
        get() = _userMobileDevice

    private var _mobileDeviceList = MutableLiveData<MutableList<MobileDevice>>()
    val mobileDeviceList: LiveData<MutableList<MobileDevice>>
        get() = _mobileDeviceList

    val recyclerViewVisibility: Int
        get() = if (_mobileDeviceList.value?.size!! > 0) View.VISIBLE else View.INVISIBLE

    val noDeviceTvVisibility: Int
        get() = if (_mobileDeviceList.value?.size!! > 0) View.INVISIBLE else View.VISIBLE


    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private fun getManagedDevice() {
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
            Timber.d("$managedDevice")
            val set = managedDevice.devices.toMutableSet()
            set.add(deviceId)
            managedDevice.devices = set.toMutableList()
            managedDeviceRepo.updateCurrentUserDeviceList(managedDevice)
            Timber.d("Device is added to collection!")

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