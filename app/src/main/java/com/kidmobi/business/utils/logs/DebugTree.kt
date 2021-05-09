package com.kidmobi.business.utils.logs

import timber.log.Timber

class DebugTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String {
        return String.format("%s::%s", element.className, element.methodName)
    }
}