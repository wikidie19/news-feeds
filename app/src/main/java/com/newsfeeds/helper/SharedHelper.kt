package com.newsfeeds.helper

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson

class SharedHelper(context: Context) {

    private val sharedPreferences: SharedPreferences
    private var editor: SharedPreferences.Editor? = null

    init {
        sharedPreferences = getSharedPreference(context)
        editor = sharedPreferences.edit()
    }

    fun getSharedPreference(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun <T> put(key: String, value: T) {
        if (value is Boolean)
            editor?.putBoolean(key, value)
        else if (value is String)
            editor?.putString(key, value)
        else if (value is Int)
            editor?.putInt(key, value)
        else if (value is Float)
            editor?.putFloat(key, value)
        else if (value is Long)
            editor?.putLong(key, value)
        else {
            throw Error("Fatal Error, unknown type value trying to be saved in SharedPreff value: $value")
        }
        editor?.commit()
    }

    fun <T> valueFrom(key: String, defaultValue: T): T? {
        if (defaultValue is Boolean)
            return sharedPreferences.getBoolean(key, defaultValue) as T
        else if (defaultValue is String)
            return sharedPreferences.getString(key, defaultValue) as T
        else if (defaultValue is Int)
            sharedPreferences.getInt(key, defaultValue)
        else if (defaultValue is Float)
            sharedPreferences.getFloat(key, defaultValue)
        else if (defaultValue is Long)
            sharedPreferences.getLong(key, defaultValue)
        else {
            throw Error("Fatal Error, unknown type value ")
        }
        return null
    }

    fun <T> putList(key: String, list: List<T>) {
        val gson = Gson()
        val json = gson.toJson(list)
        putString(key, json)
    }

    fun <T> putObj(key: String, obj: T) {
        val gson = Gson()
        val json = gson.toJson(obj)
        putString(key, json)
    }

    private fun putString(key: String, isi: String) {
        sharedPreferences.edit().putString(key, isi).apply()
    }

    fun remove(key: String) {
        editor?.remove(key)?.commit()
    }

    fun clear() {

    }

    companion object {
        val language = "language"
    }

}
