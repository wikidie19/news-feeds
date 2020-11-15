package com.newsfeeds.helper

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import com.newsfeeds.BuildConfig
import java.util.*

class ApplicationHelper(context: Context) {

    var context: Context? = context

    private val EMULATOR = "9774d56d682e549c"

    val packageName: String
        get() = context()?.packageName!!

    val resources: Resources
        get() = context()?.resources!!

    val assetManager: AssetManager
        get() = context()?.assets!!

    val lastPackageName: String
        get() = packageName.substring(packageName.lastIndexOf(".") + 1)

    val name: String?
        get() {
            try {
                val packageManager = packageManager()
                if (packageManager == null) {
                    LoggerHelper.warning("PackageManager was NULL")
                    return null
                }

                val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
                if (applicationInfo == null) {
                    LoggerHelper.warning("ApplicationInfo was NULL")
                    return null
                }

                val label = applicationInfo.loadLabel(packageManager)
                if (label == null) {
                    LoggerHelper.warning("Label was NULL")
                    return null
                }

                return label.toString()
            } catch (e: Exception) {
                LoggerHelper.wtf(e)
                return null
            }

        }

    val version: String?
        get() {
            try {
                val packageManager = packageManager()
                if (packageManager == null) {
                    LoggerHelper.warning("PackageManager was NULL")
                    return null
                }

                val applicationInfo = packageManager.getPackageInfo(packageName, 0)
                if (applicationInfo == null) {
                    LoggerHelper.warning("ApplicationInfo was NULL")
                    return null
                }

                return applicationInfo.versionName

            } catch (e: Exception) {
                LoggerHelper.wtf(e)
                return null
            }

        }

    fun context(): Context? {
        return this.context
    }

    fun packageManager(): PackageManager? {
        return context()?.packageManager
    }

    @Suppress("DEPRECATION")
    @SuppressLint("MissingPermission", "HardwareIds", "NewApi")
    fun generateId(): String {
        var deviceId = EMULATOR
        val isAllowed = PermissionHelper.requestDeviceIdPermission(context)

        if (isAllowed) {
            var tmDevice = ""
            val tm = context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val androidId =
                Settings.Secure.getString(context?.contentResolver, Settings.Secure.ANDROID_ID)
            var tmSerial = ""
            when {
                Build.VERSION.SDK_INT == Build.VERSION_CODES.Q -> {
                    tmDevice = androidId
                }
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) and (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) -> {
                    tmDevice = tm.imei
                    tmSerial = tm.simSerialNumber
                }
                Build.VERSION.SDK_INT < Build.VERSION_CODES.O -> {
                    tmDevice = tm.deviceId
                    tmSerial = tm.simSerialNumber
                }
            }
            val deviceUuid = UUID(
                androidId.hashCode().toLong(),
                tmDevice.hashCode().toLong() shl 32 or tmSerial.hashCode().toLong()
            )
            deviceId = deviceUuid.toString()

        } else {
            PermissionHelper.requestDeviceIdPermission(context)
        }

        return deviceId
    }

    companion object {
        fun isAppInDebugMode(): Boolean {
            return BuildConfig.DEBUG || BuildConfig.BUILD_TYPE.equals("debug")
        }

        fun printstackTrace(e: Throwable) {
            if (isAppInDebugMode()) {
                e.printStackTrace()
            }
        }
    }
}
