package com.kidmobi.business.utils.misc

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import com.kidmobi.data.model.InstalledApp


class RunningAppsUtil constructor(var context: Context) {

    fun getList(): MutableList<InstalledApp> {
        val list: MutableList<InstalledApp> = mutableListOf()

        val activityManager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val procInfos = activityManager.runningAppProcesses
        val pm = context.packageManager


        procInfos.forEach { info ->
            val processName = info.processName
            val appName = pm.getApplicationInfo(processName, 0).loadLabel(context.packageManager).toString()
            val app = InstalledApp(appName, processName)
            list.add(app)
        }

        return list
    }
}