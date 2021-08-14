package com.kidmobi.business.utils

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject

class SharedPrefsUtil @Inject constructor(
    @ApplicationContext private var context: Context
) {
    companion object {
        const val DEVICE_ID = "DEVICE_ID"
    }

    private fun getPrefsInstance(): SharedPreferences {
        return context.getSharedPreferences("MobiconLocalData", Context.MODE_PRIVATE)
    }

    fun setDeviceId() {
        val uid = getStringByKey(DEVICE_ID)
        if (uid.isNullOrEmpty()) {
            val deviceId = UUID.randomUUID().toString()
            setStringByKey(deviceId)
        }
    }

    fun getDeviceId(): String {
        return getStringByKey(DEVICE_ID).toString()
    }

    private fun getStringByKey(key: String): String? {
        return getPrefsInstance().getString(key, null)
    }

    private fun setStringByKey(deviceId: String) {
        val prefences = getPrefsInstance()
        val editor = prefences.edit()
        editor.putString(DEVICE_ID, deviceId).apply()
    }
}