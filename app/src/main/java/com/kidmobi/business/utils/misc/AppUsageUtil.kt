package com.kidmobi.business.utils.misc

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import java.util.*


class AppUsageUtil constructor(var context: Context) {

    fun getList(): MutableList<UsageStats> {

        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -1)
        val start: Long = calendar.timeInMillis
        val end = System.currentTimeMillis()

        val stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, start, end)
        return stats
    }

    fun getDailyUsageByPackageName(packageName: String): Long {
        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -1)
        val start: Long = calendar.timeInMillis
        val end = System.currentTimeMillis()

        val stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, start, end)

        var dailyUsage = 0L

        /*val forEach = stats?.forEach { s ->
            if (s.packageName.equals(packageName))
                dailyUsage = s.totalTimeVisible
            return@forEach
        }*/

        return dailyUsage
    }
}