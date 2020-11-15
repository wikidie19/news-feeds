package com.newsfeeds.model.local.feeds

import androidx.room.*
import com.newsfeeds.model.TimestampConverter
import java.util.*

@Entity(tableName = "feeds_data", indices = [Index(value = ["headlineMain"], unique = true)])
data class FeedsData @JvmOverloads constructor(
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