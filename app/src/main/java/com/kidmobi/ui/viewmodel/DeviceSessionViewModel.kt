package com.kidmobi.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kidmobi.data.model.DeviceSession
import com.kidmobi.data.repositories.DeviceSessionRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeviceSessionViewModel @Inject constructor(private var deviceSessionRepo: DeviceSessionRepo) : ViewModel() {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private lateinit var session: DeviceSession

    private var _currentSession = MutableLiveData<DeviceSession>()
    val currentSession: LiveData<DeviceSession>
        get() = _currentSession

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun sessionStart(session: DeviceSession) {
        uiScope.launch {
            deviceSessionRepo.add(session)
        }
    }

    fun getSession(deviceId: String) {
        uiScope.launch {
            session = deviceSessionRepo.getByOpenSession(deviceId)
            _currentSession.postValue(session)
        }
    }
}