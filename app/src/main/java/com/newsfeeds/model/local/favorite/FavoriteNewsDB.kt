package com.newsfeeds.model.local.favorite

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [FavoriteNews::class], version = 1, exportSchema = false)
@TypeConverters(TimestampConverter::class)
abstract class FavoriteNewsDB : RoomDatabase() {
    abstract fun favoriteNewsDao(): FavoriteNewsDao
}
