package com.newsfeeds.helper

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object KeyboardHelper {

    fun hideKeyboard(activity: Activity) {
        try {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(ActivityHelper.contentView(activity).windowToken, 0)
        } catch (ignored: Exception) {
        }

    }

    fun showKeyboard(edittext: View) {
        try {
            val imm =
                edittext.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(edittext, 0)
        } catch (ignored: Exception) {
        }

    }
}
