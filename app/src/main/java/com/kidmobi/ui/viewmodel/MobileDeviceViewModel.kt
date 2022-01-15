package com.kidmobi.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.kidmobi.business.utils.enums.UserType
import com.kidmobi.business.utils.extensions.modelExtensions.thisDevice
import com.kidmobi.business.utils.misc.InstalledAppsUtil
import com.kidmobi.data.model.MobileDevice
import com.kidmobi.data.model.MobileDeviceInfo
import com.kidmobi.data.model.MobileDeviceSettings
import com.kidmobi.data.repositories.MobileDeviceRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MobileDeviceViewModel @Inject constructor(var auth: FirebaseAuth, var mobileDeviceRepo: MobileDeviceRepo, @ApplicationContext var context: Context) : ViewModel() {

    private var _device = MutableLiveData<MobileDevice>()

    val device: LiveData<MobileDevice>
        get() = _device

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    @Inject
    lateinit var installedAppsUtil: InstalledAppsUtil

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
                    info = MobileDeviceInfo().thisDevice()
                    settings = MobileDeviceSettings()
                    apps = installedAppsUtil.getList(context)
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