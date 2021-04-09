package com.kidmobi.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kidmobi.assets.repositories.UserMobileDeviceRepo
import com.kidmobi.mvvm.model.MobileDevice
import com.kidmobi.mvvm.model.UserMobileDevice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserMobileDeviceViewModel @Inject constructor() : ViewModel() {
    var userMobileDevice = MutableLiveData<UserMobileDevice>()
    var mobileDeviceList = MutableLiveData<MutableList<MobileDevice>>()
    private var userMobileDeviceRepo = UserMobileDeviceRepo()

    fun getUserMobileDevice() {
        CoroutineScope(Dispatchers.Default).launch {
            val temp = userMobileDeviceRepo.getByCurrentUserId()
            userMobileDevice.postValue(temp)
        }
    }

    fun getUserMobileDevices() {
        CoroutineScope(Dispatchers.Default).launch {
            val temp = userMobileDeviceRepo.getListOfUserDevices()
            mobileDeviceList.postValue(temp)
        }
    }

    fun deleteFromMyDevices(documentId: String) {
        CoroutineScope(Dispatchers.Default).launch {
            userMobileDeviceRepo.remove(documentId)
            getUserMobileDevice()
            getUserMobileDevices()
        }
    }
}