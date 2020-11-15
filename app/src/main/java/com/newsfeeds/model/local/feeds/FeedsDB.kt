package com.newsfeeds.model.local.feeds

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.newsfeeds.model.TimestampConverter

@Database(entities = [FeedsData::class], version = 1, exportSchema = false)
@TypeConverters(TimestampConverter::class)
abstract class FeedsDB : RoomDatabase() {
    abstract fun feedsDao(): FeedsDao
}