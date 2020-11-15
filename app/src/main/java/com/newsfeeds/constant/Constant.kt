package com.newsfeeds.constant

class Constant{
    object Koin {
        const val CONTEXT_APP_DI = "context.app.newsfeeds"
        const val DATABASE_DI = "database.newsfeeds"
    }

    object DB{
        const val DB_NEWS_FEEDS = "news_feeds_db"
        const val DB_FAVORITE_NEWS = "favorite_news_db"
        const val DB_SEARCH_QUERY = "search_query_db"
    }

    object StaticUrl {
        const val baseImageUrl = "https://static01.nyt.com/"
    }

}