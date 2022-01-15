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
    /*fun getList(): MutableList<InstalledApp> {
        Timber.d("List installed app!")
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val pkgAppsList = context.packageManager.queryIntentActivities(mainIntent, 0)

        val list: MutableList<InstalledApp> = mutableListOf()

        for (p in pkgAppsList) {
            val appName = p.loadLabel(context.packageManager).toString()
            val packageName = p.resolvePackageName
            packageName?.let {
                val app = InstalledApp(appName, it)
                list.add(app)
            }
        }
        return list
    }*/

    fun getList(context: Context): MutableList<InstalledApp> {
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
}