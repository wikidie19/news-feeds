package com.newsfeeds.model.local.search

import androidx.lifecycle.LiveData

class SearchQueryRepository(private val searchQueryDao: SearchQueryDao) {

    fun saveSearchQueryData(searchQuery: SearchQuery) {
        searchQueryDao.insertSearchQueryData(searchQuery)
    }

    fun deleteAllSearchQueryData(){
        searchQueryDao.deleteAllSearchQueryData()
    }

    fun deleteItem(queryValue: String?) {
        searchQueryDao.deleteItem(queryValue)
    }

    fun loadQuerySearchData(): LiveData<List<SearchQuery>> {
        return searchQueryDao.getSearchQueryData()
    }

}
