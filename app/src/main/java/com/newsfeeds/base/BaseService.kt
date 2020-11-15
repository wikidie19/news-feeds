package com.newsfeeds.base

import android.app.Service
import android.content.Intent
import android.os.IBinder

abstract class BaseService<T : BasePresenter<*>> : Service() {

    protected var presenter: T? = null

    override fun onCreate() {
        super.onCreate()
        this.presenter = attachPresenter()
    }

    abstract fun attachPresenter(): T

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}