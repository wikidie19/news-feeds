package com.newsfeeds.model.local.search

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SearchQueryDao {

    @Query("SELECT * FROM search_query order by dateCreate ASC")
    fun getSearchQueryData(): LiveData<List<SearchQuery>>

    @Query("DELETE FROM search_query")
    fun deleteAllSearchQueryData()

    @Query("DELETE FROM search_query WHERE query_value IN (:queryValue)")
    fun deleteItem(queryValue: String?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSearchQueryData(searchQueryData: SearchQuery)

}