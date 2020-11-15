package com.newsfeeds.model.local.search

import androidx.room.*
import com.newsfeeds.model.TimestampConverter
import java.util.*

@Entity(tableName = "search_query", indices = [Index(value = ["query_value"], unique = true)])
data class SearchQuery @JvmOverloads constructor(
    @ColumnInfo(name = "query_value") var queryValue: String? = "",
    @ColumnInfo(name = "dateCreate") @TypeConverters(TimestampConverter::class) var dateCreate: Date? = null,
    @PrimaryKey @ColumnInfo(name = "entryid") var id: String = UUID.randomUUID().toString()
)