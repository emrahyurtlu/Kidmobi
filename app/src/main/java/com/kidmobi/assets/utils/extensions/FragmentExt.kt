package com.kidmobi.assets.utils.extensions

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kidmobi.R
import kotlin.system.exitProcess

fun Fragment.checkSystemSettingsAdjustable() {
    if (!Settings.System.canWrite(context)) {
        MaterialAlertDialogBuilder(context!!)
            .setTitle(getString(R.string.permission_of_changing_system_settings))
            .setMessage(getString(R.string.permission_of_system_setting_msg))
            .setCancelable(false)
            .setNegativeButton(R.string.no) { dialog, which ->
                dialog.cancel()
                dialog.dismiss()
                Toast.makeText(
                    activity?.findViewById(android.R.id.content),
                    getString(R.string.no_permission_msg),
                    Toast.LENGTH_LONG
                ).show()
                exitProcess(-1)
            }
            .setPositiveButton(R.string.yes) { dialog, which ->
                if (!Settings.System.canWrite(context)) {
                    Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).also {
                        it.data = Uri.parse("package:${context!!.packageName}")
                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(it)
                    }
                }
                dialog.dismiss()
            }
            .show()
    }
}