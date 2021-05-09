package com.kidmobi.business.utils.logs

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class ReleaseTree : Timber.Tree() {
    /*
    We dont wanna log some messages for info, verbose and debug
     */
    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return !(priority == Log.INFO || priority == Log.VERBOSE || priority == Log.DEBUG)
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        FirebaseCrashlytics.getInstance().also {
            it.setCustomKey(PRIORITY, priority)
            tag?.let { t ->
                it.setCustomKey(TAG, t)
            }
            it.log(message)
            t?.let { e ->
                it.recordException(e)
            }
        }.sendUnsentReports()
    }

    companion object {
        const val PRIORITY = "priority"
        const val TAG = "tag"
    }
}