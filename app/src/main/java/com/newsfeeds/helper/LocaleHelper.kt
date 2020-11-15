package com.newsfeeds.helper

import android.content.Context
import android.os.Build
import java.util.*
import android.content.res.Configuration
import android.annotation.SuppressLint
import android.content.res.Resources

object LocaleHelper {

    fun setLocale(context: Context?): Context {
        return updateResources(context!!, getLanguage(context))
    }

    fun setNewLocale(context: Context, language: String): Context {
        persistLanguage(context, language)
        return updateResources(context, language)
    }

    fun getLanguage(c: Context): String {
        return SharedHelper(c).valueFrom(SharedHelper.language, "en") as String
    }

    @SuppressLint("ApplySharedPref")
    private fun persistLanguage(context: Context, language: String) {
        SharedHelper(context).put(SharedHelper.language, language)
    }

    private fun updateResources(context: Context, language: String): Context {
        var cont = context
        val locale = Locale(language)
        Locale.setDefault(locale)

        val res = context.resources
        val config = Configuration(res.configuration)
        if (Build.VERSION.SDK_INT >= 21) {
            config.setLocale(locale)
            cont = context.createConfigurationContext(config)
        } else {
            config.locale = locale
            res.updateConfiguration(config, res.displayMetrics)
        }
        return cont
    }

    fun getLocale(res: Resources): Locale {
        val config = res.getConfiguration()
        return if (Build.VERSION.SDK_INT >= 24) config.getLocales().get(0) else config.locale
    }

}