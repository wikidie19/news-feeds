package com.newsfeeds.model.local.feeds

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FeedsDao {

    @Query("SELECT * FROM feeds_data order by dateCreate ASC")
    fun getFeedsData(): LiveData<List<FeedsData>>

    @Query("DELETE FROM feeds_data")
    fun deleteAllFeedsData()

    @Query("DELETE FROM feeds_data WHERE headlineMain IN (:headlineMain)")
    fun deleteItem(headlineMain: String?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFeedsData(feedsData: FeedsData)

}