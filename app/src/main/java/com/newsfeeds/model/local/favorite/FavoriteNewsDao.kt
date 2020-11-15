package com.newsfeeds.model.local.favorite

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteNewsDao {

    @Query("SELECT * FROM favorite_news order by dateCreate ASC")
    fun getFavoriteNews(): LiveData<List<FavoriteNews>>

    @Query("DELETE FROM favorite_news")
    fun deleteAllFavoriteNews()

    @Query("DELETE FROM favorite_news WHERE headlineMain IN (:headlineMain)")
    fun deleteItem(headlineMain: String?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteNews(favoriteNews: FavoriteNews)

}
