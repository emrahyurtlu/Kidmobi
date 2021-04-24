package com.kidmobi.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.kidmobi.assets.repositories.DeviceSessionRepo
import com.kidmobi.mvvm.model.DeviceSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeviceSessionViewModel @Inject constructor(var deviceSessionRepo: DeviceSessionRepo) : ViewModel() {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun sessionStart(session: DeviceSession) {
        uiScope.launch {
            deviceSessionRepo.add(session)
        }
    }
}