package com.newsfeeds.model.local.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.newsfeeds.extention.map

class FavoriteNewsViewModel(private val favoriteNewsRepository: FavoriteNewsRepository) : ViewModel() {

    private lateinit var favoriteNewsResult: LiveData<List<FavoriteNews>>

    init {
        subscribeFavoriteNewsResult()
    }

    fun saveFavoriteNews(favoriteNews: FavoriteNews) {
        favoriteNewsRepository.saveFavoriteNews(favoriteNews)
    }

    fun deleteAllFavoriteNews() {
        favoriteNewsRepository.deleteAllFavoriteNews()
    }

    fun deleteItem(headlineMain: String?) {
        favoriteNewsRepository.deleteItem(headlineMain)
    }

    fun listenFavoriteNewsResult(): LiveData<List<FavoriteNews>> {
        return favoriteNewsResult
    }

    private fun subscribeFavoriteNewsResult() {
        favoriteNewsResult = favoriteNewsRepository.loadFavoriteNews().map { data ->
            data.reversed().map {
                FavoriteNews(
                    it.headlineMain,
                    it.abstract,
                    it.snippet,
                    it.lead_paragraph,
                    it.source,
                    it.multimedia,
                    it.headline,
                    it.pubDate,
                    it.byline,
                    it.dateCreate
                )
            }
        }
    }
}
