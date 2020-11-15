package com.newsfeeds.base

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.multidex.MultiDexApplication
import com.newsfeeds.helper.LocaleHelper

class BaseApplication : MultiDexApplication() {

    private var topActivity: Activity? = null

    override fun onCreate() {
        super.onCreate()

        register()

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleHelper.setLocale(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(LocaleHelper.setLocale(base))
    }

    private fun register() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity) {
                topActivity = activity
            }

            override fun onActivityResumed(activity: Activity) {
                topActivity = activity
            }

            override fun onActivityStarted(activity: Activity) {
                topActivity = activity
            }

            override fun onActivityDestroyed(activity: Activity) {
                topActivity = activity
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

            }

            override fun onActivityStopped(activity: Activity) {

            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

            }

        })
    }

    companion object {
        private var activityVisible: Boolean = false

        fun isActivityVisible(): Boolean {
            return activityVisible
        }

        fun activityResumed() {
            activityVisible = true
        }

        fun activityOnStart() {
            activityVisible = true
        }

        fun activityPaused() {
            activityVisible = false
        }
    }
}
