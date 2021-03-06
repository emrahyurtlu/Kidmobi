package com.kidmobi.business.utils.extensions

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kidmobi.R
import kotlin.system.exitProcess

fun Activity.hideSoftKeyboard() {
    currentFocus?.let {
        val inputMethodManager =
            ContextCompat.getSystemService(this, InputMethodManager::class.java)!!
        inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

fun Activity.goto(target: Class<*>, finish: Boolean = false) {
    Intent(this, target).also {
        this.startActivity(it)
        if (finish)
            finish()
    }
}

fun Activity.checkSystemSettingsAdjustable() {
    if (!Settings.System.canWrite(this)) {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.permission_of_changing_system_settings))
            .setMessage(getString(R.string.permission_of_system_setting_msg))
            .setCancelable(false)
            .setNegativeButton(R.string.no) { dialog, _ ->
                dialog.cancel()
                dialog.dismiss()
                Toast.makeText(
                    findViewById(android.R.id.content),
                    getString(R.string.no_permission_msg),
                    Toast.LENGTH_LONG
                ).show()
                exitProcess(-1)
            }
            .setPositiveButton(R.string.yes) { dialog, _ ->
                if (!Settings.System.canWrite(this)) {
                    Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).also {
                        it.data = Uri.parse("package:$packageName")
                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(it)
                    }
                }
                dialog.dismiss()
            }
            .show()
    }
}