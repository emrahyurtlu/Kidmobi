package com.kidmobi.business.utils

import android.annotation.SuppressLint
import android.util.Log
import timber.log.Timber

class CrashReportingTree : Timber.Tree() {
    @SuppressLint("LogNotTimber")
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.DEBUG || priority == Log.ERROR || priority == Log.INFO) {
            Log.e("KIDMOBI", "Log was secured")
        }
    }
}
