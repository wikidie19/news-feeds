package com.newsfeeds.network

import com.newsfeeds.MainApplication
import com.newsfeeds.R
import java.io.IOException

class NoConnectivityException : IOException() {
    override val message: String
        get() = MainApplication.context.getString(R.string.no_connection)
}