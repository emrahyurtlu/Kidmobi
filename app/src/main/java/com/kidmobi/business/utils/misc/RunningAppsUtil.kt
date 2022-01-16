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

    /*fun getActiveApps(context: Context): MutableList<InstalledApp> {
        val list: MutableList<InstalledApp> = mutableListOf()
        val pm = context.packageManager
        val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)
        for (packageInfo in packages) {
            //system apps! get out
            if (!isSTOPPED(packageInfo) && !isSYSTEM(packageInfo)) {
                val packageName = packageInfo.packageName
                val appName = getApplicationLabel(context, packageName).toString()
                val app = InstalledApp(appName, packageName)
                list.add(app)
            }
        }
        return list
    }

    private fun isSTOPPED(pkgInfo: ApplicationInfo): Boolean {
        return pkgInfo.flags and ApplicationInfo.FLAG_STOPPED != 0
    }

    private fun isSYSTEM(pkgInfo: ApplicationInfo): Boolean {
        return pkgInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }

    private fun getApplicationLabel(context: Context, packageName: String): String? {
        val packageManager = context.packageManager
        val packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        var label: String? = null
        for (i in packages.indices) {
            val temp = packages[i]
            if (temp.packageName == packageName) label = packageManager.getApplicationLabel(temp).toString()
        }
        return label
    }

    private fun getForegroundApp(): RunningAppProcessInfo? {
        var activityManager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        var result: RunningAppProcessInfo? = null
        var info: RunningAppProcessInfo? = null
        val l: List<RunningAppProcessInfo> = activityManager.runningAppProcesses
        val i = l.iterator()
        while (i.hasNext()) {
            info = i.next()
            if (info.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                && !isRunningService(info.processName)
            ) {
                result = info
                break
            }
        }
        return result
    }

    private fun getActivityForApp(target: RunningAppProcessInfo?): ComponentName? {
        var result: ComponentName? = null
        var info: RunningTaskInfo
        val activityManager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        if (target == null) return null
        val l: List<RunningTaskInfo> = activityManager.getRunningTasks(9999)
        val i = l.iterator()
        while (i.hasNext()) {
            info = i.next()
            if (info.baseActivity!!.packageName == target.processName) {
                result = info.topActivity
                break
            }
        }
        return result
    }

    private fun isStillActive(process: RunningAppProcessInfo?, activity: ComponentName?): Boolean {
        // activity can be null in cases, where one app starts another. for example, astro
        // starting rock player when a move file was clicked. we dont have an activity then,
        // but the package exits as soon as back is hit. so we can ignore the activity
        // in this case
        if (process == null) return false
        val currentFg = getForegroundApp()
        val currentActivity = getActivityForApp(currentFg)
        if (currentFg != null && currentFg.processName == process.processName &&
            (activity == null || currentActivity!!.compareTo(activity) == 0)
        ) return true
        Timber.d(
            "isStillActive returns false - CallerProcess: " + process.processName + " CurrentProcess: "
                    + (if (currentFg == null) "null" else currentFg.processName) + " CallerActivity:" + (activity?.toString() ?: "null")
                    + " CurrentActivity: " + (currentActivity?.toString() ?: "null")
        )
        return false
    }

    private fun isRunningService(processname: String?): Boolean {
        if (processname == null || processname.isEmpty()) return false
        var service: ActivityManager.RunningServiceInfo
        val activityManager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val l: List<ActivityManager.RunningServiceInfo> = activityManager.getRunningServices(9999)
        val i: Iterator<ActivityManager.RunningServiceInfo> = l.iterator()
        while (i.hasNext()) {
            service = i.next()
            if (service.process.equals(processname)) return true
        }
        return false
    }*/

}