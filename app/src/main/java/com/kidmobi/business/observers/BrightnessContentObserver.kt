package com.kidmobi.business.observers

import android.content.Context
import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import android.telephony.TelephonyManager


class BrightnessContentObserver(val context: Context?, val funcParam: () -> Unit) : ContentObserver(Handler(Looper.getMainLooper())) {
    private val manager = context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

}