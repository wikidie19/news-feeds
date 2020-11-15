package com.newsfeeds.model.local.favorite

import androidx.room.*
import java.util.*

@Entity(tableName = "favorite_news", indices = [Index(value = ["headlineMain"], unique = true)])
data class FavoriteNews @JvmOverloads constructor(
    @ColumnInfo(name = "headlineMain") var headlineMain: String? = "",
    @ColumnInfo(name = "abstract") var abstract: String? = "",
    @ColumnInfo(name = "snippet") var snippet: String? = "",
    @ColumnInfo(name = "lead_paragraph") var lead_paragraph: String? = "",
    @ColumnInfo(name = "source") var source: String? = "",
    @ColumnInfo(name = "multimedia") var multimedia: String? = "",
    @ColumnInfo(name = "headline") var headline: String? = "",
    @ColumnInfo(name = "pub_date") var pubDate: String? = "",
    @ColumnInfo(name = "byline") var byline: String? = "",
    @ColumnInfo(name = "dateCreate") @TypeConverters(TimestampConverter::class) var dateCreate: Date? = null,
    @PrimaryKey @ColumnInfo(name = "entryid") var id: String = UUID.randomUUID().toString()
)

class TimestampConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
