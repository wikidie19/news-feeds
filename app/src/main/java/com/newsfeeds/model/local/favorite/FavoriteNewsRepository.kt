package com.newsfeeds.model.local.favorite

import androidx.lifecycle.LiveData

class FavoriteNewsRepository(private val favoriteNewsDao: FavoriteNewsDao) {

    fun saveFavoriteNews(favoriteNews: FavoriteNews) {
        favoriteNewsDao.insertFavoriteNews(favoriteNews)
    }

    fun deleteAllFavoriteNews(){
        favoriteNewsDao.deleteAllFavoriteNews()
    }

    fun deleteItem(headlineMain: String?) {
        favoriteNewsDao.deleteItem(headlineMain)
    }

    fun loadFavoriteNews(): LiveData<List<FavoriteNews>> {
        return favoriteNewsDao.getFavoriteNews()
    }

}
