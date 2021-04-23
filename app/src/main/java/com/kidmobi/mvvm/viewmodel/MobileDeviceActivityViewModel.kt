package com.kidmobi.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.kidmobi.assets.repositories.MobileDeviceRepo
import com.kidmobi.assets.repositories.UserMobileDeviceRepo
import com.kidmobi.mvvm.model.MobileDevice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MobileDeviceActivityViewModel @Inject constructor(var mobileDeviceRepo: MobileDeviceRepo, var userMobileDeviceRepo: UserMobileDeviceRepo) : ViewModel() {

    fun saveDeviceDetails(device: MobileDevice) {
        Timber.d("$device")
        CoroutineScope(Dispatchers.Default).launch {
            val calendar = Calendar.getInstance()
            device.updatedAt = calendar.time

            mobileDeviceRepo.update(device.deviceId, device)

            val umd = userMobileDeviceRepo.getByCurrentUserId()

            if (!umd.devices.contains(device.deviceId)) {
                umd.devices.add(device.deviceId)
                userMobileDeviceRepo.updateCurrentUserDeviceList(umd)
            }
        }
    }

}