package com.kidmobi.business.utils.misc

import android.content.Context
import android.content.pm.PackageInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class InstalledAppsUtil @Inject constructor(
    @ApplicationContext private var context: Context
) {
    fun getList(): MutableList<PackageInfo> {
        val packageManager = context.packageManager
        return packageManager.getInstalledPackages(0)
    }
}