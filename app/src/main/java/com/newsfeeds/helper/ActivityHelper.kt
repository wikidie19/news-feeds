package com.newsfeeds.helper

import android.app.Activity
import android.view.View

class ActivityHelper protected constructor() {

    companion object {
        fun contentView(activity: Activity): View {
            return activity.findViewById(android.R.id.content)
        }
    }
}