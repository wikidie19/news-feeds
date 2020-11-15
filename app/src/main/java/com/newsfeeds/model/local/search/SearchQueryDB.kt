package com.newsfeeds.model.local.search

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.newsfeeds.model.TimestampConverter

@Database(entities = [SearchQuery::class], version = 1, exportSchema = false)
@TypeConverters(TimestampConverter::class)
abstract class SearchQueryDB : RoomDatabase() {
    abstract fun searchQueryDao(): SearchQueryDao
}