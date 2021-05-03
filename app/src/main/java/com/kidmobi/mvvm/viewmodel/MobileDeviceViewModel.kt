package com.kidmobi.mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.kidmobi.business.enums.UserType
import com.kidmobi.business.repositories.MobileDeviceRepo
import com.kidmobi.business.utils.extensions.modelExtensions.init
import com.kidmobi.mvvm.model.MobileDevice
import com.kidmobi.mvvm.model.MobileDeviceInfo
import com.kidmobi.mvvm.model.MobileDeviceSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MobileDeviceViewModel @Inject constructor(var auth: FirebaseAuth, var mobileDeviceRepo: MobileDeviceRepo) : ViewModel() {

    private var _device = MutableLiveData<MobileDevice>()

    val device: LiveData<MobileDevice>
        get() = _device

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun saveDeviceInitially(uniqueDeviceId: String) {
        uiScope.launch {
            val auth = FirebaseAuth.getInstance()
            auth.currentUser?.let { user ->
                val now = Calendar.getInstance()
                val temp = MobileDevice()
                temp.apply {
                    deviceId = uniqueDeviceId
                    info = MobileDeviceInfo().init()
                    settings = MobileDeviceSettings().init()
                    createdAt = now.time
                    updatedAt = now.time
                    deviceOwnerName = user.displayName.toString()
                    deviceOwnerImageUrl = user.photoUrl.toString()
                    deviceOwnerEmail = user.email.toString()
                    deviceOwnerUid = user.uid
                    userType = when (user.providerId) {
                        "google.com" -> UserType.UserFromGoogle
                        "facebook.com" -> UserType.UserFromFacebook
                        "firebase" -> UserType.UserAnonymous
                        else -> UserType.UserUnknown
                    }
                }

                val docExist = mobileDeviceRepo.docExists(uniqueDeviceId)
                if (!docExist)
                    mobileDeviceRepo.add(temp)
            }
        }
    }

    fun getDeviceById(deviceId: String) {
        uiScope.launch {
            val result = mobileDeviceRepo.getById(deviceId)
            _device.postValue(result)
        }
    }

    fun updateDevice(device: MobileDevice) {
        val calendar = Calendar.getInstance()
        device.updatedAt = calendar.time
        uiScope.launch {
            mobileDeviceRepo.update(documentId = device.deviceId, entity = device)
            Timber.d("updateDevice: $device")
        }
    }
}