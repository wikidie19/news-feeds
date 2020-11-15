package com.newsfeeds.model

import androidx.room.Room
import com.newsfeeds.constant.Constant
import com.newsfeeds.model.local.favorite.FavoriteNewsDB
import com.newsfeeds.model.local.favorite.FavoriteNewsRepository
import com.newsfeeds.model.local.favorite.FavoriteNewsViewModel
import com.newsfeeds.model.local.feeds.FeedsDB
import com.newsfeeds.model.local.feeds.FeedsRepository
import com.newsfeeds.model.local.feeds.FeedsViewModel
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext


val favoriteNewsAppModule = applicationContext {
    bean(name = Constant.Koin.DATABASE_DI) {
        Room.databaseBuilder(
            get(Constant.Koin.CONTEXT_APP_DI),
            FavoriteNewsDB::class.java,
            Constant.DB.DB_FAVORITE_NEWS
        ).fallbackToDestructiveMigration().build()
    }

    bean { get<FavoriteNewsDB>(Constant.Koin.DATABASE_DI).favoriteNewsDao() }
    bean { FavoriteNewsRepository(get()) }

    viewModel { FavoriteNewsViewModel(get()) }
}

val feedsAppModule = applicationContext {
    bean(name = Constant.Koin.DATABASE_DI) {
        Room.databaseBuilder(
            get(Constant.Koin.CONTEXT_APP_DI),
            FeedsDB::class.java,
            Constant.DB.DB_NEWS_FEEDS
        ).fallbackToDestructiveMigration().build()
    }

    bean { get<FeedsDB>(Constant.Koin.DATABASE_DI).feedsDao() }
    bean { FeedsRepository(get()) }

    viewModel { FeedsViewModel(get()) }
}