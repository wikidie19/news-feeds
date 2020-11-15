package com.newsfeeds

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.newsfeeds.constant.Constant
import com.newsfeeds.model.favoriteNewsAppModule
import com.newsfeeds.model.feedsAppModule
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.applicationContext

class MainApplication : MultiDexApplication() {

    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        initKoin()
    }

    private fun initKoin() {
        startKoin(
            this,
            listOf(
                applicationModule,
                favoriteNewsAppModule,
                feedsAppModule
            )
        )
    }

    private val applicationModule = applicationContext {
        bean(Constant.Koin.CONTEXT_APP_DI) { this@MainApplication.applicationContext }
    }

}