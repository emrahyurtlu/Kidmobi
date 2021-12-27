package com.kidmobi.business.utils.misc

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.kidmobi.data.model.InstalledApp
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject


class InstalledAppsUtil @Inject constructor(
    @ApplicationContext private var context: Context
) {
    fun getList(): MutableList<InstalledApp> {
        /*getInstalledApps()
        val list: MutableList<InstalledApp> = mutableListOf()
        val packageManager = context.packageManager
        val packages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)
        for (p in packages) {
            if (p.applicationInfo.flags != ApplicationInfo.FLAG_SYSTEM && p.applicationInfo.enabled) {
                val appName = p.applicationInfo.loadLabel(packageManager).toString()
                val packageName = p.applicationInfo.packageName
                val app = InstalledApp(appName, packageName)
                list.add(app)
            }
        }

        println(list)

        return list*/

        Timber.d("List installed app!")
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val pkgAppsList = context.packageManager.queryIntentActivities(mainIntent, 0)

        val list: MutableList<InstalledApp> = mutableListOf()

        for (p in pkgAppsList) {
            val appName = p.loadLabel(context.packageManager).toString()
            val packageName = p.resolvePackageName
            val app = InstalledApp(appName, packageName)
            list.add(app)
        }

        return list
    }
}