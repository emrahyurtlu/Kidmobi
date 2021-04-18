package com.kidmobi.assets.receivers

import android.app.admin.DeviceAdminReceiver
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.kidmobi.mvvm.view.DashboardActivity
import timber.log.Timber


class DeviceOwnerReceiver : DeviceAdminReceiver() {

    override fun onEnabled(context: Context, intent: Intent) {
        super.onEnabled(context, intent)
        Timber.d("onEnabled")
    }

    override fun onProfileProvisioningComplete(context: Context, intent: Intent) {
        super.onProfileProvisioningComplete(context, intent)
        Timber.d("onProfileProvisioningComplete")
        // Enable the profile
        val manager = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val componentName = getComponentName(context)
        manager.setProfileName(componentName, "Sample Device Owner")

        Settings.System.putInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL)
        Settings.System.putInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS, 10)

        // Open the main screen
        val launch = Intent(context, DashboardActivity::class.java)
        launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(launch)
    }

    /**
     * @return A newly instantiated [android.content.ComponentName] for this
     * DeviceAdminReceiver.
     */
    companion object {
        fun getComponentName(context: Context): ComponentName {
            return ComponentName(context.applicationContext, DeviceOwnerReceiver::class.java)
        }
    }
}